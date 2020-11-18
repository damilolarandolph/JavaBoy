package JavaBoy.cpu;

import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.instructions.Instruction;
import JavaBoy.cpu.registers.RegisterBank;
import JavaBoy.cpu.registers.RegisterPairs;
import JavaBoy.memory.MemoryMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.OptionalInt;
import java.util.Scanner;

public class CPU {
    private final MemoryMap mmu;

    File file = new File("/home/damilola/gameboy.txt");

    FileWriter writer;

    private final Instruction[] instructions;

    private final RegisterBank registers;

    private int cycles = 0;

    public CPU(MemoryMap mmu, Instruction[] instructions, RegisterBank registers) {
        try{
            writer = new FileWriter(file);
        }catch (Exception err){
            System.err.println(err.getMessage());
        }
        this.mmu = mmu;
        this.instructions = instructions;
        this.registers = registers;
        this.setPC(0x100);
    }


    public void run() {
        Scanner scan = new Scanner(System.in);
        int opcode = readPC();
       // printState(opcode);
        while (tryExecute(opcode)) {
            opcode = readPC();
            //printState(opcode);

        }

    }

    private boolean tryExecute(int opcode) {
        OptionalInt result = OptionalInt.empty();

        for (Instruction instruction : this.instructions) {
            result = instruction.execute(opcode, this);
            if (result.isPresent()) {
                this.cycles += result.getAsInt();
                break;
            }
        }

        if (result.isEmpty()){
            System.err.println("Unknown Instruction: " + getHex(opcode));
        }

        return true;

        //return result.isPresent();

    }


    public int readPC() {
        int addr = this.registers.getPC().read();
        this.registers.getPC().increment();
        return this.mmu.readByte(addr);
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
        this.mmu.setByte(address, value);
    }

    public int readAddress(int address) {
        return this.mmu.readByte(address);
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

        string += String.format("PC: %s SP: %s \n", getHex(getPC()), getHex(getSP()));

        string += String.format("Opcode: %s \n", getHex(opcode));

        string += String.format("F: [%s%s%s%s]\n\n",
                isFlag(FLAGS.Z) ? "Z" : "-",
                isFlag(FLAGS.H) ? "H" : "-",
                isFlag(FLAGS.N) ? "N" : "-",
                isFlag(FLAGS.C) ? "C" : "-"
        );
        try {
            writer.write(string);
            writer.flush();
        }catch (Exception err){
            System.err.println("Couldn't write " + err.getMessage());
        }


    }

    private String getHex(int i) {
        return Integer.toHexString(i);
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
        this.mmu.setByte(addr, value);
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

    }

    public void disableInterrupts() {

    }

    public int getPC() {
        return this.registers.getPC().read();
    }

    public void setPC(int word) {
        this.registers.getPC().write(word);
    }
}
