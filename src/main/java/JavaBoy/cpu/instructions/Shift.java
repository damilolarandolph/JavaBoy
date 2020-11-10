package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class Shift implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0xcb) {
            int cbOpcode = cpu.readByteFromPC();
            switch (cbOpcode) {
                case 0x27:
                    return sla(REGISTER.A, cpu);
                case 0x20:
                    return sla(REGISTER.B, cpu);
                case 0x21:
                    return sla(REGISTER.C, cpu);
                case 0x22:
                    return sla(REGISTER.D, cpu);
                case 0x23:
                    return sla(REGISTER.E, cpu);
                case 0x24:
                    return sla(REGISTER.H, cpu);
                case 0x25:
                    return sla(REGISTER.L, cpu);
                case 0x26:
                    return sla(cpu);
                case 0x2f:
                    return sra(REGISTER.A, cpu);
                case 0x28:
                    return sra(REGISTER.B, cpu);
                case 0x29:
                    return sra(REGISTER.C, cpu);
                case 0x2a:
                    return sra(REGISTER.D, cpu);
                case 0x2b:
                    return sra(REGISTER.E, cpu);
                case 0x2c:
                    return sra(REGISTER.H, cpu);
                case 0x2d:
                    return sra(REGISTER.L, cpu);
                case 0x2e:
                    return sra(cpu);
                case 0x3f:
                    return srl(REGISTER.A, cpu);
                case 0x38:
                    return srl(REGISTER.B, cpu);
                case 0x39:
                    return srl(REGISTER.C, cpu);
                case 0x3a:
                    return srl(REGISTER.D, cpu);
                case 0x3b:
                    return srl(REGISTER.E, cpu);
                case 0x3c:
                    return srl(REGISTER.H, cpu);
                case 0x3d:
                    return srl(REGISTER.L, cpu);
                case 0x3e:
                    return srl(cpu);

            }

        }

        return OptionalInt.empty();
    }


    private int applySRA(int value, CPU cpu) {
        int msb = value & 0x80;
        int lsb = value & 0x01;

        int result = (value >>> 1) | msb;

        cpu.writeFlag(FLAG.Cy, lsb);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);

        return result;
    }

    private int applySRL(int value, CPU cpu) {
        int lsb = value & 0x01;
        int result = value >>> 1;
        cpu.writeFlag(FLAG.Cy, lsb);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);
        return result;
    }

    private OptionalInt srl(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);

        int result = applySRL(bits, cpu);

        cpu.writeByteRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt srl(CPU cpu){
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readFromAddress(addr);
        int result = applySRL(bits, cpu);
        cpu.writeToAddress(addr, result);
        return OptionalInt.of(16);
    }

    private OptionalInt sra(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);
        int result = applySRA(bits, cpu);
        cpu.writeByteRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt sra(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readFromAddress(addr);
        int result = applySRA(bits, cpu);
        cpu.writeToAddress(addr, result);

        return OptionalInt.of(16);

    }

    private int applySLA(int value, CPU cpu) {
        int msb = value >>> 7;
        int result = value << 1;

        cpu.writeFlag(FLAG.Cy, msb);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);

        return result;

    }

    private OptionalInt sla(REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);

        int result = applySLA(bits, cpu);

        cpu.writeByteRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt sla(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readFromAddress(addr);
        int result = applySLA(bits, cpu);
        cpu.writeToAddress(addr, result);

        return OptionalInt.of(16);
    }
}
