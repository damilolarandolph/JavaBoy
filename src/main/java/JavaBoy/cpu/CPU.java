package JavaBoy.cpu;

import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.instructions.Instruction;
import JavaBoy.cpu.interrupts.InterruptManager;
import JavaBoy.cpu.registers.RegisterBank;
import JavaBoy.cpu.registers.RegisterPairs;
import JavaBoy.memory.Dma;
import JavaBoy.memory.MemoryMap;
import JavaBoy.timer.Timer;
import JavaBoy.video.Gpu;

import java.io.File;
import java.io.FileWriter;

public class CPU {
    private final MemoryMap mmu;
    private final Instruction[] instructions;
    private final RegisterBank registers;
    private final InterruptManager interruptManager;
    private final Gpu gpu;
    private final Timer timer;
    private final Dma dma;
    File file = new File("/home/damilola/gameboy.txt");
    FileWriter writer;
    private boolean halted = false;

    public CPU(MemoryMap mmu, Instruction[] instructions,
               RegisterBank registers, InterruptManager interruptManager,
               Timer timer, Dma dma, Gpu gpu) {
        try {
            writer = new FileWriter(file);
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
        this.timer = timer;
        this.gpu = gpu;
        this.mmu = mmu;
        this.dma = dma;
        this.interruptManager = interruptManager;
        this.instructions = instructions;
        this.registers = registers;
        this.setPC(0x100);
    }


    public void run() {
        int opcode;
        boolean canRun = true;
        while (canRun) {
            boolean didInterrupt = interruptManager.handleInterrupts(this);
            if (halted) {
                if (didInterrupt) {
                    halted = false;
                } else if (
                        interruptManager.hasServiceableInterrupts() &&
                                !interruptManager.isMasterEnabled()) {
                    int currentPc = getPC();
                    halted = false;
                    tryExecute(readPC());
                    setPC(currentPc);
                }
                addCycles();
            } else {

                opcode = readPC();
                canRun = tryExecute(opcode);
            }
        }
        //printState(opcode);


    }


    public void addCycles() {
        addCycles(1);
    }

    public void addCycles(int multiple) {

        for (int count = multiple * 4; count >= 0; --count) {
            timer.tick();
            dma.tick();
            gpu.tick();
        }

    }

    private boolean tryExecute(int opcode) {
        //printState(opcode);
        if (getPC() == 0xc002) {
            System.out.println("hey");
        }
        boolean executed = false;
        if (opcode == 0x76) {
            halted = true;
            executed = true;
            addCycles();
        } else {
            for (Instruction instruction : this.instructions) {
                executed = instruction.execute(opcode, this);
                if (executed)
                    break;
            }
        }

        if (!executed) {
            System.err.println("Unknown Instruction: " + getHex(opcode));
        }

        return true;

        //return result.isPresent();

    }


    public int readPC() {
        int addr = this.registers.getPC().read();
        this.registers.getPC().increment();
        return getByte(addr);

    }

    private int getByte(int addr) {
        if(dma.canAccess(addr))
        return this.mmu.readByte(addr);
        else
            return 0;
    }

    private void writeByte(int addr, int value) {
        if(dma.canAccess(addr))
        this.mmu.setByte(addr, value);
    }


    public int readWordPC() {
        int lsb = this.readPC();
        int msb = this.readPC();
        return (msb << 8) | lsb;
    }

    public boolean isFlag(FLAGS flag) {
        return this.registers.getFlags().isFlag(flag);
    }

    public void writeAddress(int address, int value) {
        writeByte(address, value);
    }

    public int readAddress(int address) {
        return getByte(address);
    }

    private void printState(int opcode) {
        String string = "";
        string += String.format("A: %s F: %s (AF: %s)\n",
                                getHex(readRegister(REGISTERS.A)),
                                getHex(readRegister(REGISTERS.F)),
                                getHex(readWordRegister(RegisterPairs.AF)));

        string += String.format("B: %s C: %s (BC: %s)\n",
                                getHex(readRegister(REGISTERS.B)),
                                getHex(readRegister(REGISTERS.C)),
                                getHex(readWordRegister(RegisterPairs.BC)));

        string += String.format("D: %s E: %s (DE: %s)\n",
                                getHex(readRegister(REGISTERS.D)),
                                getHex(readRegister(REGISTERS.E)),
                                getHex(readWordRegister(RegisterPairs.DE)));

        string += String.format("H: %s L: %s (HL: %s)\n",
                                getHex(readRegister(REGISTERS.H)),
                                getHex(readRegister(REGISTERS.L)),
                                getHex(readWordRegister(RegisterPairs.HL)));

        string += String.format("PC: %s SP: %s \n", getHex(getPC()),
                                getHex(getSP()));

        string += String.format("Opcode: %s \n", getHex(opcode));


        string += String.format("TIMA: %s \n", getHex(timer.getTIMA()));

        string += String.format("F: [%s%s%s%s]\n\n",
                                isFlag(FLAGS.Z) ? "Z" : "-",
                                isFlag(FLAGS.H) ? "H" : "-",
                                isFlag(FLAGS.N) ? "N" : "-",
                                isFlag(FLAGS.C) ? "C" : "-"
        );
        try {
            writer.write(string);
            writer.flush();
        } catch (Exception err) {
            System.err.println("Couldn't write " + err.getMessage());
        }


    }

    private String getHex(int i) {
        return "0x" + Integer.toHexString(i).toUpperCase();
    }

    public int getSP() {
        return this.registers.getSP().read();
    }

    public void setSP(int val) {
        this.registers.getSP().write(val);
    }

    public void pushSP(int value) {
        this.registers.getSP().decrement();
        int addr = this.getSP();
        writeByte(addr, value);
    }

    public int popSP() {
        int addr = this.getSP();
        int value = this.readAddress(addr);
        this.registers.getSP().increment();
        return value;
    }

    public void writeRegister(REGISTERS reg, int value) {
        this.registers.writeRegister(reg, value);
    }

    public int readRegister(REGISTERS reg) {
        return this.registers.readRegister(reg);
    }

    public int readWordRegister(RegisterPairs pair) {
        return this.registers.readRegister(pair);
    }

    public void writeWordRegister(RegisterPairs pair, int value) {
        this.registers.writeRegister(pair, value);
    }


    public int getFlag(FLAGS flag) {
        return this.registers.getFlags().getFlag(flag);
    }

    public void setFlag(FLAGS flag, boolean value) {
        this.registers.getFlags().setFlag(flag, value);
    }

    public void enableInterrupts() {
        this.interruptManager.enableInterrupts();
    }

    public void disableInterrupts() {
        this.interruptManager.disableInterrupts();
    }

    public int getPC() {
        return this.registers.getPC().read();
    }

    public void setPC(int word) {
        this.registers.getPC().write(word);
    }
}
