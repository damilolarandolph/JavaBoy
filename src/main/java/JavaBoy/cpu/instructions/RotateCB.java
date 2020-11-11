package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class RotateCB implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x07:
                return rlc(REGISTER.A, cpu);
            case 0x00:
                return rlc(REGISTER.B, cpu);
            case 0x01:
                return rlc(REGISTER.C, cpu);
            case 0x02:
                return rlc(REGISTER.D, cpu);
            case 0x03:
                return rlc(REGISTER.E, cpu);
            case 0x04:
                return rlc(REGISTER.H, cpu);
            case 0x05:
                return rlc(cpu);
            case 0x17:
                return rl(REGISTER.A, cpu);
            case 0x10:
                return rl(REGISTER.B, cpu);
            case 0x11:
                return rl(REGISTER.C, cpu);
            case 0x12:
                return rl(REGISTER.D, cpu);
            case 0x13:
                return rl(REGISTER.E, cpu);
            case 0x14:
                return rl(REGISTER.H, cpu);
            case 0x16:
                return rl(cpu);
            case 0x0f:
                return rrc(REGISTER.A, cpu);
            case 0x08:
                return rrc(REGISTER.B, cpu);
            case 0x09:
                return rrc(REGISTER.C, cpu);
            case 0x0a:
                return rrc(REGISTER.D, cpu);
            case 0x0b:
                return rrc(REGISTER.E, cpu);
            case 0x0c:
                return rrc(REGISTER.H, cpu);
            case 0x0d:
                return rrc(REGISTER.L, cpu);
            case 0x0e:
                return rrc(cpu);
            case 0x1f:
                return rr(REGISTER.A, cpu);
            case 0x18:
                return rr(REGISTER.B, cpu);
            case 0x19:
                return rr(REGISTER.C, cpu);
            case 0x1a:
                return rr(REGISTER.D, cpu);
            case 0x1b:
                return rr(REGISTER.E, cpu);
            case 0x1c:
                return rr(REGISTER.H, cpu);
            case 0x1d:
                return rr(REGISTER.L, cpu);
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

        cpu.writeFlag(FLAG.Cy, lsb);
        return bits;
    }
    private int applyRotateR(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (cpu.getFlag(FLAG.Cy) << 7) | bits;
        cpu.writeFlag(FLAG.Cy, lsb);
        return bits;
    }
    private int applyRotateLC(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        cpu.writeFlag(FLAG.Cy, msb);
        bits = (bits << 1) & 0xff;
        bits = bits | msb;
        return bits;
    }
    private int applyRotateL(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        bits = (bits << 1) & 0xff;
        bits = bits | cpu.getFlag(FLAG.Cy);
        cpu.writeFlag(FLAG.Cy, msb);
        return bits;
    }


    private OptionalInt rlc(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readFromAddress(addr);

        int result = applyRotateLC(bits, cpu);
        cpu.writeToAddress(addr, result);

        if (result == 0)
            cpu.setFlag(FLAG.Z);
        else
            cpu.resetFlag(FLAG.Z);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(16);
    }


    private OptionalInt rlc(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);

        int result = applyRotateLC(bits, cpu);

        cpu.writeByteRegister(reg, result);

        if (result == 0)
            cpu.setFlag(FLAG.Z);
        else
            cpu.resetFlag(FLAG.Z);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rl(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);
        int result = applyRotateL(bits, cpu);
        cpu.writeByteRegister(reg, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rrc(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);
        int result = applyRotateRC(bits, cpu);
        cpu.writeByteRegister(reg, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rrc(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));

        int bits = cpu.readFromAddress(addr);
        int result = applyRotateRC(bits, cpu);
        cpu.writeToAddress(addr, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(16);

    }

    private OptionalInt rr(REGISTER reg, CPU cpu){
        int bits = cpu.readByteRegister(reg);
        int result = applyRotateR(bits, cpu);
        cpu.writeByteRegister(reg, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(8);
    }

    private OptionalInt rr(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));

        int bits = cpu.readFromAddress(addr);
        int result = applyRotateR(bits, cpu);
        cpu.writeToAddress(addr, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(16);
    }

    private OptionalInt rl(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));

        int bits = cpu.readFromAddress(addr);
        int result = applyRotateL(bits, cpu);
        cpu.writeToAddress(addr, result);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return OptionalInt.of(16);
    }
}