package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class RotateCB implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
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
                return OptionalInt.empty();

        }
    }

    private int applyRotateRC(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (lsb << 7) | bits;

        cpu.writeFlag(FLAGS.Cy, lsb);
        return bits;
    }
    private int applyRotateR(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (cpu.getFlag(FLAGS.Cy) << 7) | bits;
        cpu.writeFlag(FLAGS.Cy, lsb);
        return bits;
    }
    private int applyRotateLC(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        cpu.writeFlag(FLAGS.Cy, msb);
        bits = (bits << 1) & 0xff;
        bits = bits | msb;
        return bits;
    }
    private int applyRotateL(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        bits = (bits << 1) & 0xff;
        bits = bits | cpu.getFlag(FLAGS.Cy);
        cpu.writeFlag(FLAGS.Cy, msb);
        return bits;
    }


    private OptionalInt rlc(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);

        int result = applyRotateLC(bits, cpu);
        cpu.writeAddress(addr, result);

        if (result == 0)
            cpu.setFlag(FLAGS.Z);
        else
            cpu.resetFlag(FLAGS.Z);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(16);
    }


    private OptionalInt rlc(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);

        int result = applyRotateLC(bits, cpu);

        cpu.writeRegister(reg, result);

        if (result == 0)
            cpu.setFlag(FLAGS.Z);
        else
            cpu.resetFlag(FLAGS.Z);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rl(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateL(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rrc(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applyRotateRC(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rrc(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));

        int bits = cpu.readAddress(addr);
        int result = applyRotateRC(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(16);

    }

    private OptionalInt rr(REGISTERS reg, CPU cpu){
        int bits = cpu.readRegister(reg);
        int result = applyRotateR(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rr(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));

        int bits = cpu.readAddress(addr);
        int result = applyRotateR(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(16);
    }

    private OptionalInt rl(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));

        int bits = cpu.readAddress(addr);
        int result = applyRotateL(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        return OptionalInt.of(16);
    }
}
