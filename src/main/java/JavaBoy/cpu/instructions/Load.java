package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import static JavaBoy.utils.ArithmeticUtils.isCarry16;
import static JavaBoy.utils.ArithmeticUtils.isHalfCarry16;
import static JavaBoy.utils.BitUtils.getLsb;
import static JavaBoy.utils.BitUtils.getMsb;

public class Load implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            //Register To Register Loads
            case 0x7f:
                return load(REGISTERS.A, REGISTERS.A, cpu);
            case 0x78:
                return load(REGISTERS.A, REGISTERS.B, cpu);
            case 0x79:
                return load(REGISTERS.A, REGISTERS.C, cpu);
            case 0x7a:
                return load(REGISTERS.A, REGISTERS.D, cpu);
            case 0x7b:
                return load(REGISTERS.A, REGISTERS.E, cpu);
            case 0x7c:
                return load(REGISTERS.A, REGISTERS.H, cpu);
            case 0x7d:
                return load(REGISTERS.A, REGISTERS.L, cpu);
            case 0x47:
                return load(REGISTERS.B, REGISTERS.A, cpu);
            case 0x40:
                return load(REGISTERS.B, REGISTERS.B, cpu);
            case 0x41:
                return load(REGISTERS.B, REGISTERS.C, cpu);
            case 0x42:
                return load(REGISTERS.B, REGISTERS.D, cpu);
            case 0x43:
                return load(REGISTERS.B, REGISTERS.E, cpu);
            case 0x44:
                return load(REGISTERS.B, REGISTERS.H, cpu);
            case 0x45:
                return load(REGISTERS.B, REGISTERS.L, cpu);
            case 0x4f:
                return load(REGISTERS.C, REGISTERS.A, cpu);
            case 0x48:
                return load(REGISTERS.C, REGISTERS.B, cpu);
            case 0x49:
                return load(REGISTERS.C, REGISTERS.C, cpu);
            case 0x4a:
                return load(REGISTERS.C, REGISTERS.D, cpu);
            case 0x4b:
                return load(REGISTERS.C, REGISTERS.E, cpu);
            case 0x4c:
                return load(REGISTERS.C, REGISTERS.H, cpu);
            case 0x4d:
                return load(REGISTERS.C, REGISTERS.L, cpu);
            case 0x57:
                return load(REGISTERS.D, REGISTERS.A, cpu);
            case 0x50:
                return load(REGISTERS.D, REGISTERS.B, cpu);
            case 0x51:
                return load(REGISTERS.D, REGISTERS.C, cpu);
            case 0x52:
                return load(REGISTERS.D, REGISTERS.D, cpu);
            case 0x53:
                return load(REGISTERS.D, REGISTERS.E, cpu);
            case 0x54:
                return load(REGISTERS.D, REGISTERS.H, cpu);
            case 0x55:
                return load(REGISTERS.D, REGISTERS.L, cpu);
            case 0x5f:
                return load(REGISTERS.E, REGISTERS.A, cpu);
            case 0x58:
                return load(REGISTERS.E, REGISTERS.B, cpu);
            case 0x59:
                return load(REGISTERS.E, REGISTERS.C, cpu);
            case 0x5a:
                return load(REGISTERS.E, REGISTERS.D, cpu);
            case 0x5b:
                return load(REGISTERS.E, REGISTERS.E, cpu);
            case 0x5c:
                return load(REGISTERS.E, REGISTERS.H, cpu);
            case 0x5d:
                return load(REGISTERS.E, REGISTERS.L, cpu);
            case 0x67:
                return load(REGISTERS.H, REGISTERS.A, cpu);
            case 0x60:
                return load(REGISTERS.H, REGISTERS.B, cpu);
            case 0x61:
                return load(REGISTERS.H, REGISTERS.C, cpu);
            case 0x62:
                return load(REGISTERS.H, REGISTERS.D, cpu);
            case 0x63:
                return load(REGISTERS.H, REGISTERS.E, cpu);
            case 0x64:
                return load(REGISTERS.H, REGISTERS.H, cpu);
            case 0x65:
                return load(REGISTERS.H, REGISTERS.L, cpu);
            case 0x6f:
                return load(REGISTERS.L, REGISTERS.A, cpu);
            case 0x68:
                return load(REGISTERS.L, REGISTERS.B, cpu);
            case 0x69:
                return load(REGISTERS.L, REGISTERS.C, cpu);
            case 0x6a:
                return load(REGISTERS.L, REGISTERS.D, cpu);
            case 0x6b:
                return load(REGISTERS.L, REGISTERS.E, cpu);
            case 0x6c:
                return load(REGISTERS.L, REGISTERS.H, cpu);
            case 0x6d:
                return load(REGISTERS.L, REGISTERS.L, cpu);
            //LD r, n  immediate byte to register loads
            case 0x3e:
                return load(REGISTERS.A, cpu);
            case 0x06:
                return load(REGISTERS.B, cpu);
            case 0x0e:
                return load(REGISTERS.C, cpu);
            case 0x16:
                return load(REGISTERS.D, cpu);
            case 0x1e:
                return load(REGISTERS.E, cpu);
            case 0x26:
                return load(REGISTERS.H, cpu);
            case 0x2e:
                return load(REGISTERS.L, cpu);
            //LD (HL), r register into (HL)
            case 0x77:
                return loadHL(REGISTERS.A, cpu);
            case 0x70:
                return loadHL(REGISTERS.B, cpu);
            case 0x71:
                return loadHL(REGISTERS.C, cpu);
            case 0x72:
                return loadHL(REGISTERS.D, cpu);
            case 0x73:
                return loadHL(REGISTERS.E, cpu);
            case 0x74:
                return loadHL(REGISTERS.H, cpu);
            case 0x75:
                return loadHL(REGISTERS.L, cpu);
            //LD (HL), n   imm into (HL)
            case 0x36:
                return loadHL(cpu);
            //LD r, (HL)   (HL) into r
            case 0x7e:
                return loadRegHL(REGISTERS.A, cpu);
            case 0x46:
                return loadRegHL(REGISTERS.B, cpu);
            case 0x4e:
                return loadRegHL(REGISTERS.C, cpu);
            case 0x56:
                return loadRegHL(REGISTERS.D, cpu);
            case 0x5e:
                return loadRegHL(REGISTERS.E, cpu);
            case 0x66:
                return loadRegHL(REGISTERS.H, cpu);
            case 0x6e:
                return loadRegHL(REGISTERS.L, cpu);
            //LD A, (SS)    (SS) into A
            case 0x0a:
                return loadA(RegisterPairs.BC, cpu);
            case 0x1a:
                return loadA(RegisterPairs.DE, cpu);
            case 0xfa:
                return loadAImmWord(cpu);
            //LD (SS), A  A into (SS)
            case 0x02:
                return loadPair(RegisterPairs.BC, cpu);
            case 0x12:
                return loadPair(RegisterPairs.DE, cpu);
            case 0xea:
                return loadImmWordA(cpu);
            // LD   A,(FF00+n)
            case 0xf0:
                return loadAImm(cpu);
            //(FF00+n),A
            case 0xe0:
                return loadImmA(cpu);
            case 0xf2:
                return loadOffsetC(cpu);
            case 0xe2:
                return loadOffsetA(cpu);
            case 0x22:
                return loadHLI(cpu);
            case 0x32:
                return loadHLD(cpu);
            case 0x2a:
                return loadAHLI(cpu);
            case 0x3a:
                return loadAHLD(cpu);
            //16 bit loads
            case 0x01:
                return load16(RegisterPairs.BC, cpu);
            case 0x11:
                return load16(RegisterPairs.DE, cpu);
            case 0x21:
                return load16(RegisterPairs.HL, cpu);
            case 0x31:
                return load16SP(cpu);
            case 0xf9:
                return load16SPHL(cpu);
            case 0x08:
                return load16SPAddr(cpu);
            case 0xf8:
                return ldhl(cpu);
            default:
                return false;

        }


    }


    private boolean load(REGISTERS reg1, REGISTERS reg2, CPU cpu) {
        int value = cpu.readRegister(reg2);
        cpu.writeRegister(reg1, value);
        cpu.addCycles();
        return true;
    }

    private boolean load(REGISTERS reg, CPU cpu) {
        int value = cpu.readPC();
        cpu.writeRegister(reg, value);
        cpu.addCycles(3);
        return true;
    }

    private boolean loadRegHL(REGISTERS reg, CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readAddress(addr);

        cpu.writeRegister(reg, value);
        cpu.addCycles(2);
        return true;
    }


    private boolean loadHL(REGISTERS reg, CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readRegister(reg);

        cpu.writeAddress(addr, value);
        cpu.addCycles(2);
        return true;
    }

    private boolean loadHL(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readPC();

        cpu.writeAddress(addr, value);
        cpu.addCycles(3);
        return true;
    }

    private boolean loadA(RegisterPairs pair, CPU cpu) {
        int addr = cpu.readWordRegister(pair);
        int value = cpu.readAddress(addr);
        cpu.writeRegister(REGISTERS.A, value);
        cpu.addCycles(2);
        return true;
    }

    private boolean loadOffsetC(CPU cpu) {

        int address = 0xff00 + cpu.readRegister(REGISTERS.C);
        cpu.writeRegister(REGISTERS.A, cpu.readAddress(address));
        cpu.addCycles(2);
        return true;
    }

    private boolean loadOffsetA(CPU cpu) {
        int address = 0xff00 + cpu.readRegister(REGISTERS.C);
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.addCycles(2);
        return true;
    }

    private boolean loadAImm(CPU cpu) {
        int offset = cpu.readPC();
        int address = (0xff00 + offset) & 0xffff;
        int val = cpu.readAddress(address);

        cpu.writeRegister(REGISTERS.A, val);
        cpu.addCycles(3);
        return true;
    }

    private boolean loadImmA(CPU cpu) {
        int offset = cpu.readPC();
        int address = (0xff00 + offset) & 0xffff;
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.addCycles(3);
        return true;
    }

    private boolean loadAImmWord(CPU cpu) {
        int address = cpu.readWordPC();

        cpu.writeRegister(REGISTERS.A, cpu.readAddress(address));
        cpu.addCycles(4);
        return true;
    }

    private boolean loadImmWordA(CPU cpu) {
        int address = cpu.readWordPC();
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.addCycles(4);
        return true;
    }

    private boolean loadAHLI(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readAddress(address);
        cpu.writeRegister(REGISTERS.A, value);
        cpu.writeWordRegister(RegisterPairs.HL, address + 1);
        cpu.addCycles(2);
        return true;
    }

    private boolean loadAHLD(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readAddress(address);
        cpu.writeRegister(REGISTERS.A, value);
        cpu.writeWordRegister(RegisterPairs.HL, address - 1);
        cpu.addCycles(2);
        return true;
    }

    private boolean loadPair(RegisterPairs pair, CPU cpu) {
        int address = cpu.readWordRegister(pair);
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.addCycles(2);
        return true;
    }

    private boolean loadHLD(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.writeWordRegister(RegisterPairs.HL, address - 1);
        cpu.addCycles(2);
        return true;
    }

    private boolean loadHLI(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        cpu.writeAddress(address, cpu.readRegister(REGISTERS.A));
        cpu.writeWordRegister(RegisterPairs.HL, address + 1);
        cpu.addCycles(2);
        return true;
    }

    private boolean load16(RegisterPairs pair, CPU cpu) {
        int word = cpu.readWordPC();
        cpu.writeWordRegister(pair, word);
        cpu.addCycles(3);
        return true;
    }

    private boolean load16SP(CPU cpu) {
        int word = cpu.readWordPC();
        cpu.setSP(word);
        cpu.addCycles(3);
        return true;
    }

    private boolean load16SPHL(CPU cpu) {
        cpu.setSP(cpu.readWordRegister(RegisterPairs.HL));
        cpu.addCycles(2);
        return true;
    }

    private boolean ldhl(CPU cpu) {
        int value = (byte) cpu.readPC();
        int currentSP = cpu.getSP();
        cpu.writeWordRegister(RegisterPairs.HL, currentSP + value);

        cpu.setFlag(FLAGS.C, isCarry16(currentSP, value));
        cpu.setFlag(FLAGS.H, isHalfCarry16(currentSP, value));
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles(3);
        return true;
    }

    private boolean load16SPAddr(CPU cpu) {
        int addr = cpu.readWordPC();

        cpu.writeAddress(addr, getLsb(cpu.getSP()));
        cpu.writeAddress(addr + 1, getMsb(cpu.getSP()));
        cpu.addCycles(5);
        return true;
    }

}
