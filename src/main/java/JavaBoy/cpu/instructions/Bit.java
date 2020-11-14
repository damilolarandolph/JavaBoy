package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class Bit implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x47:
                return bit(0, REGISTERS.A, cpu);
            case 0x40:
                return bit(0, REGISTERS.B, cpu);
            case 0x41:
                return bit(0, REGISTERS.C, cpu);
            case 0x42:
                return bit(0, REGISTERS.D, cpu);
            case 0x43:
                return bit(0, REGISTERS.E, cpu);
            case 0x44:
                return bit(0, REGISTERS.H, cpu);
            case 0x45:
                return bit(0, REGISTERS.L, cpu);
            case 0x4f:
                return bit(1, REGISTERS.A, cpu);
            case 0x48:
                return bit(1, REGISTERS.B, cpu);
            case 0x49:
                return bit(1, REGISTERS.C, cpu);
            case 0x4a:
                return bit(1, REGISTERS.D, cpu);
            case 0x4b:
                return bit(1, REGISTERS.E, cpu);
            case 0x4c:
                return bit(1, REGISTERS.H, cpu);
            case 0x4d:
                return bit(1, REGISTERS.L, cpu);
            case 0x57:
                return bit(2, REGISTERS.A, cpu);
            case 0x50:
                return bit(2, REGISTERS.B, cpu);
            case 0x51:
                return bit(2, REGISTERS.C, cpu);
            case 0x52:
                return bit(2, REGISTERS.D, cpu);
            case 0x53:
                return bit(2, REGISTERS.E, cpu);
            case 0x54:
                return bit(2, REGISTERS.H, cpu);
            case 0x55:
                return bit(2, REGISTERS.L, cpu);
            case 0x5f:
                return bit(3, REGISTERS.A, cpu);
            case 0x58:
                return bit(3, REGISTERS.B, cpu);
            case 0x59:
                return bit(3, REGISTERS.C, cpu);
            case 0x5a:
                return bit(3, REGISTERS.D, cpu);
            case 0x5b:
                return bit(3, REGISTERS.E, cpu);
            case 0x5c:
                return bit(3, REGISTERS.H, cpu);
            case 0x5d:
                return bit(3, REGISTERS.L, cpu);
            case 0x67:
                return bit(4, REGISTERS.A, cpu);
            case 0x60:
                return bit(4, REGISTERS.B, cpu);
            case 0x61:
                return bit(4, REGISTERS.C, cpu);
            case 0x62:
                return bit(4, REGISTERS.D, cpu);
            case 0x63:
                return bit(4, REGISTERS.E, cpu);
            case 0x64:
                return bit(4, REGISTERS.H, cpu);
            case 0x65:
                return bit(4, REGISTERS.L, cpu);
            case 0x6f:
                return bit(5, REGISTERS.A, cpu);
            case 0x68:
                return bit(5, REGISTERS.B, cpu);
            case 0x69:
                return bit(5, REGISTERS.C, cpu);
            case 0x6a:
                return bit(5, REGISTERS.D, cpu);
            case 0x6b:
                return bit(5, REGISTERS.E, cpu);
            case 0x6c:
                return bit(5, REGISTERS.H, cpu);
            case 0x6d:
                return bit(5, REGISTERS.L, cpu);
            case 0x77:
                return bit(6, REGISTERS.A, cpu);
            case 0x70:
                return bit(6, REGISTERS.B, cpu);
            case 0x71:
                return bit(6, REGISTERS.C, cpu);
            case 0x72:
                return bit(6, REGISTERS.D, cpu);
            case 0x73:
                return bit(6, REGISTERS.E, cpu);
            case 0x74:
                return bit(6, REGISTERS.H, cpu);
            case 0x75:
                return bit(6, REGISTERS.L, cpu);
            case 0x7f:
                return bit(7, REGISTERS.A, cpu);
            case 0x78:
                return bit(7, REGISTERS.B, cpu);
            case 0x79:
                return bit(7, REGISTERS.C, cpu);
            case 0x7a:
                return bit(7, REGISTERS.D, cpu);
            case 0x7b:
                return bit(7, REGISTERS.E, cpu);
            case 0x7c:
                return bit(7, REGISTERS.H, cpu);
            case 0x7d:
                return bit(7, REGISTERS.L, cpu);
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
        cpu.writeFlag(FLAGS.Z, ~(bit));
        cpu.setFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
    }

    private OptionalInt bit(int testBit, REGISTERS REGISTERS, CPU cpu) {
        int bits = cpu.readRegister(REGISTERS);
        applyBitTest(testBit, bits, cpu);
        return OptionalInt.of(8);
    }

    private OptionalInt bit(int testBit, CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);
        applyBitTest(testBit, bits, cpu);
        return OptionalInt.of(12);
    }


}
