package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAG;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class Shift implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
            switch (opcode) {
                case 0x27:
                    return sla(REGISTERS.A, cpu);
                case 0x20:
                    return sla(REGISTERS.B, cpu);
                case 0x21:
                    return sla(REGISTERS.C, cpu);
                case 0x22:
                    return sla(REGISTERS.D, cpu);
                case 0x23:
                    return sla(REGISTERS.E, cpu);
                case 0x24:
                    return sla(REGISTERS.H, cpu);
                case 0x25:
                    return sla(REGISTERS.L, cpu);
                case 0x26:
                    return sla(cpu);
                case 0x2f:
                    return sra(REGISTERS.A, cpu);
                case 0x28:
                    return sra(REGISTERS.B, cpu);
                case 0x29:
                    return sra(REGISTERS.C, cpu);
                case 0x2a:
                    return sra(REGISTERS.D, cpu);
                case 0x2b:
                    return sra(REGISTERS.E, cpu);
                case 0x2c:
                    return sra(REGISTERS.H, cpu);
                case 0x2d:
                    return sra(REGISTERS.L, cpu);
                case 0x2e:
                    return sra(cpu);
                case 0x3f:
                    return srl(REGISTERS.A, cpu);
                case 0x38:
                    return srl(REGISTERS.B, cpu);
                case 0x39:
                    return srl(REGISTERS.C, cpu);
                case 0x3a:
                    return srl(REGISTERS.D, cpu);
                case 0x3b:
                    return srl(REGISTERS.E, cpu);
                case 0x3c:
                    return srl(REGISTERS.H, cpu);
                case 0x3d:
                    return srl(REGISTERS.L, cpu);
                case 0x3e:
                    return srl(cpu);

                default:
                    return OptionalInt.empty();

            }


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

    private OptionalInt srl(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);

        int result = applySRL(bits, cpu);

        cpu.writeRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt srl(CPU cpu){
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);
        int result = applySRL(bits, cpu);
        cpu.writeAddress(addr, result);
        return OptionalInt.of(16);
    }

    private OptionalInt sra(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applySRA(bits, cpu);
        cpu.writeRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt sra(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);
        int result = applySRA(bits, cpu);
        cpu.writeAddress(addr, result);

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

    private OptionalInt sla(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);

        int result = applySLA(bits, cpu);

        cpu.writeRegister(reg, result);

        return OptionalInt.of(8);
    }

    private OptionalInt sla(CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);
        int result = applySLA(bits, cpu);
        cpu.writeAddress(addr, result);

        return OptionalInt.of(16);
    }
}
