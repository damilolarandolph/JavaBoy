package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class Bit implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x47:
                return bit(0, REGISTER.A, cpu);
            case 0x40:
                return bit(0, REGISTER.B, cpu);
            case 0x41:
                return bit(0, REGISTER.C, cpu);
            case 0x42:
                return bit(0, REGISTER.D, cpu);
            case 0x43:
                return bit(0, REGISTER.E, cpu);
            case 0x44:
                return bit(0, REGISTER.H, cpu);
            case 0x45:
                return bit(0, REGISTER.L, cpu);
            case 0x4f:
                return bit(1, REGISTER.A, cpu);
            case 0x48:
                return bit(1, REGISTER.B, cpu);
            case 0x49:
                return bit(1, REGISTER.C, cpu);
            case 0x4a:
                return bit(1, REGISTER.D, cpu);
            case 0x4b:
                return bit(1, REGISTER.E, cpu);
            case 0x4c:
                return bit(1, REGISTER.H, cpu);
            case 0x4d:
                return bit(1, REGISTER.L, cpu);
            case 0x57:
                return bit(2, REGISTER.A, cpu);
            case 0x50:
                return bit(2, REGISTER.B, cpu);
            case 0x51:
                return bit(2, REGISTER.C, cpu);
            case 0x52:
                return bit(2, REGISTER.D, cpu);
            case 0x53:
                return bit(2, REGISTER.E, cpu);
            case 0x54:
                return bit(2, REGISTER.H, cpu);
            case 0x55:
                return bit(2, REGISTER.L, cpu);
            case 0x5f:
                return bit(3, REGISTER.A, cpu);
            case 0x58:
                return bit(3, REGISTER.B, cpu);
            case 0x59:
                return bit(3, REGISTER.C, cpu);
            case 0x5a:
                return bit(3, REGISTER.D, cpu);
            case 0x5b:
                return bit(3, REGISTER.E, cpu);
            case 0x5c:
                return bit(3, REGISTER.H, cpu);
            case 0x5d:
                return bit(3, REGISTER.L, cpu);
            case 0x67:
                return bit(4, REGISTER.A, cpu);
            case 0x60:
                return bit(4, REGISTER.B, cpu);
            case 0x61:
                return bit(4, REGISTER.C, cpu);
            case 0x62:
                return bit(4, REGISTER.D, cpu);
            case 0x63:
                return bit(4, REGISTER.E, cpu);
            case 0x64:
                return bit(4, REGISTER.H, cpu);
            case 0x65:
                return bit(4, REGISTER.L, cpu);
            case 0x6f:
                return bit(5, REGISTER.A, cpu);
            case 0x68:
                return bit(5, REGISTER.B, cpu);
            case 0x69:
                return bit(5, REGISTER.C, cpu);
            case 0x6a:
                return bit(5, REGISTER.D, cpu);
            case 0x6b:
                return bit(5, REGISTER.E, cpu);
            case 0x6c:
                return bit(5, REGISTER.H, cpu);
            case 0x6d:
                return bit(5, REGISTER.L, cpu);
            case 0x77:
                return bit(6, REGISTER.A, cpu);
            case 0x70:
                return bit(6, REGISTER.B, cpu);
            case 0x71:
                return bit(6, REGISTER.C, cpu);
            case 0x72:
                return bit(6, REGISTER.D, cpu);
            case 0x73:
                return bit(6, REGISTER.E, cpu);
            case 0x74:
                return bit(6, REGISTER.H, cpu);
            case 0x75:
                return bit(6, REGISTER.L, cpu);
            case 0x7f:
                return bit(7, REGISTER.A, cpu);
            case 0x78:
                return bit(7, REGISTER.B, cpu);
            case 0x79:
                return bit(7, REGISTER.C, cpu);
            case 0x7a:
                return bit(7, REGISTER.D, cpu);
            case 0x7b:
                return bit(7, REGISTER.E, cpu);
            case 0x7c:
                return bit(7, REGISTER.H, cpu);
            case 0x7d:
                return bit(7, REGISTER.L, cpu);
            case 0x46:
                return bit(0, cpu);
            case 0x4e:
                return bit(1, cpu);
            case 0x56:
                return bit(2, cpu);
            case 0x5e:
                return bit(3, cpu);
            case 0x66:
                return bit(4, cpu);
            case 0x6e:
                return bit(5, cpu);
            case 0x76:
                return bit(6, cpu);
            case 0x7e:
                return bit(7, cpu);




            default:
                return OptionalInt.empty();
        }
    }


    private void applyBitTest(int testBit, int value, CPU cpu) {
        int bit = (value >>> testBit) & 0x01;
        cpu.writeFlag(FLAG.Z, ~(bit));
        cpu.setFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
    }

    private OptionalInt bit(int testBit, REGISTER register, CPU cpu) {
        int bits = cpu.readRegister(register);
        applyBitTest(testBit, bits, cpu);
        return OptionalInt.of(8);
    }

    private OptionalInt bit(int testBit, CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readAddress(addr);
        applyBitTest(testBit, bits, cpu);
        return OptionalInt.of(12);
    }


}
