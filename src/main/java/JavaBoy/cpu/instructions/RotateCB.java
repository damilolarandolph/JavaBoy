package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

public class RotateCB implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x07:
                return rlc(REGISTERS.A, cpu);
            case 0x00:
                return rlc(REGISTERS.B, cpu);
            case 0x01:
                return rlc(REGISTERS.C, cpu);
            case 0x02:
                return rlc(REGISTERS.D, cpu);
            case 0x03:
                return rlc(REGISTERS.E, cpu);
            case 0x04:
                return rlc(REGISTERS.H, cpu);
            case 0x05:
                return rlc(cpu);
            case 0x17:
                return rl(REGISTERS.A, cpu);
            case 0x10:
                return rl(REGISTERS.B, cpu);
            case 0x11:
                return rl(REGISTERS.C, cpu);
            case 0x12:
                return rl(REGISTERS.D, cpu);
            case 0x13:
                return rl(REGISTERS.E, cpu);
            case 0x14:
                return rl(REGISTERS.H, cpu);
            case 0x16:
                return rl(cpu);
            case 0x0f:
                return rrc(REGISTERS.A, cpu);
            case 0x08:
                return rrc(REGISTERS.B, cpu);
            case 0x09:
                return rrc(REGISTERS.C, cpu);
            case 0x0a:
                return rrc(REGISTERS.D, cpu);
            case 0x0b:
                return rrc(REGISTERS.E, cpu);
            case 0x0c:
                return rrc(REGISTERS.H, cpu);
            case 0x0d:
                return rrc(REGISTERS.L, cpu);
            case 0x0e:
                return rrc(cpu);
            case 0x1f:
                return rr(REGISTERS.A, cpu);
            case 0x18:
                return rr(REGISTERS.B, cpu);
            case 0x19:
                return rr(REGISTERS.C, cpu);
            case 0x1a:
                return rr(REGISTERS.D, cpu);
            case 0x1b:
                return rr(REGISTERS.E, cpu);
            case 0x1c:
                return rr(REGISTERS.H, cpu);
            case 0x1d:
                return rr(REGISTERS.L, cpu);
            case 0x1e:
                return rr(cpu);
            default:
                return false;

        }
    }

    private int applyRotateRC(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (lsb << 7) | bits;

        cpu.setFlag(FLAGS.C, lsb == 1);
        setFlags(bits, cpu);
        return bits;
    }

    private int applyRotateR(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (cpu.getFlag(FLAGS.C) << 7) | bits;
        cpu.setFlag(FLAGS.C, lsb == 1);

        setFlags(bits, cpu);
        return bits;
    }

    private int applyRotateLC(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        cpu.setFlag(FLAGS.C, msb == 1);
        bits = (bits << 1) & 0xff;
        bits = bits | msb;

        setFlags(bits, cpu);
        return bits;
    }

    private int applyRotateL(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        bits = (bits << 1) & 0xff;
        bits = bits | cpu.getFlag(FLAGS.C);
        cpu.setFlag(FLAGS.C, msb == 1);

        setFlags(bits, cpu);
        return bits;
    }


    private void setFlags(int result, CPU cpu) {
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
    }

    private boolean rlc(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        int result = applyRotateLC(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.addCycles(4);
        return true;
    }


    private boolean rlc(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateLC(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.addCycles(2);
        return true;
    }

    private boolean rl(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateL(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.addCycles(2);
        return true;
    }

    private boolean rrc(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateRC(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.addCycles(2);
        return true;
    }

    private boolean rrc(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        int result = applyRotateRC(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.addCycles(4);
        return true;
    }

    private boolean rr(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateR(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.addCycles(2);
        return true;
    }

    private boolean rr(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        int result = applyRotateR(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.addCycles(4);
        return true;
    }

    private boolean rl(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        int result = applyRotateL(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.addCycles(4);
        return true;
    }
}
