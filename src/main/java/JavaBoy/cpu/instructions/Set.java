package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class Set implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xc7:
                return set(0, REGISTER.A, cpu);
            case 0xc0:
                return set(0, REGISTER.B, cpu);
            case 0xc1:
                return set(0, REGISTER.C, cpu);
            case 0xc2:
                return set(0, REGISTER.D, cpu);
            case 0xc3:
                return set(0, REGISTER.E, cpu);
            case 0xc4:
                return set(0, REGISTER.H, cpu);
            case 0xc5:
                return set(0, REGISTER.L, cpu);
            case 0xcf:
                return set(1, REGISTER.A, cpu);
            case 0xc8:
                return set(1, REGISTER.B, cpu);
            case 0xc9:
                return set(1, REGISTER.C, cpu);
            case 0xca:
                return set(1, REGISTER.D, cpu);
            case 0xcb:
                return set(1, REGISTER.E, cpu);
            case 0xcc:
                return set(1, REGISTER.H, cpu);
            case 0xcd:
                return set(1, REGISTER.L, cpu);
            case 0xd7:
                return set(2, REGISTER.A, cpu);
            case 0xd0:
                return set(2, REGISTER.B, cpu);
            case 0xd1:
                return set(2, REGISTER.C, cpu);
            case 0xd2:
                return set(2, REGISTER.D, cpu);
            case 0xd3:
                return set(2, REGISTER.E, cpu);
            case 0xd4:
                return set(2, REGISTER.H, cpu);
            case 0xd5:
                return set(2, REGISTER.L, cpu);
            case 0xdf:
                return set(3, REGISTER.A, cpu);
            case 0xd8:
                return set(3, REGISTER.B, cpu);
            case 0xd9:
                return set(3, REGISTER.C, cpu);
            case 0xda:
                return set(3, REGISTER.D, cpu);
            case 0xdb:
                return set(3, REGISTER.E, cpu);
            case 0xdc:
                return set(3, REGISTER.H, cpu);
            case 0xdd:
                return set(3, REGISTER.L, cpu);
            case 0xe7:
                return set(4, REGISTER.A, cpu);
            case 0xe0:
                return set(4, REGISTER.B, cpu);
            case 0xe1:
                return set(4, REGISTER.C, cpu);
            case 0xe2:
                return set(4, REGISTER.D, cpu);
            case 0xe3:
                return set(4, REGISTER.E, cpu);
            case 0xe4:
                return set(4, REGISTER.H, cpu);
            case 0xe5:
                return set(4, REGISTER.L, cpu);
            case 0xef:
                return set(5, REGISTER.A, cpu);
            case 0xe8:
                return set(5, REGISTER.B, cpu);
            case 0xe9:
                return set(5, REGISTER.C, cpu);
            case 0xea:
                return set(5, REGISTER.D, cpu);
            case 0xeb:
                return set(5, REGISTER.E, cpu);
            case 0xec:
                return set(5, REGISTER.H, cpu);
            case 0xed:
                return set(5, REGISTER.L, cpu);
            case 0xf7:
                return set(6, REGISTER.A, cpu);
            case 0xf0:
                return set(6, REGISTER.B, cpu);
            case 0xf1:
                return set(6, REGISTER.C, cpu);
            case 0xf2:
                return set(6, REGISTER.D, cpu);
            case 0xf3:
                return set(6, REGISTER.E, cpu);
            case 0xf4:
                return set(6, REGISTER.H, cpu);
            case 0xf5:
                return set(6, REGISTER.L, cpu);
            case 0xff:
                return set(7, REGISTER.A, cpu);
            case 0xf8:
                return set(7, REGISTER.B, cpu);
            case 0xf9:
                return set(7, REGISTER.C, cpu);
            case 0xfa:
                return set(7, REGISTER.D, cpu);
            case 0xfb:
                return set(7, REGISTER.E, cpu);
            case 0xfc:
                return set(7, REGISTER.H, cpu);
            case 0xfd:
                return set(7, REGISTER.L, cpu);
            case 0xc6:
                return set(0, cpu);
            case 0xce:
                return set(1, cpu);
            case 0xd6:
                return set(2, cpu);
            case 0xde:
                return set(3, cpu);
            case 0xe6:
                return set(4, cpu);
            case 0xee:
                return set(5, cpu);
            case 0xf6:
                return set(6, cpu);
            case 0xfe:
                return set(7, cpu);

            default:
                return OptionalInt.empty();
        }
    }


    private int applySet(int setBit, int value) {
        int setBitFlip = 0x01 << setBit;
        return setBitFlip | value;
    }

    private OptionalInt set(int setBit, REGISTER reg, CPU cpu) {
        int bits = cpu.readByteRegister(reg);
        cpu.writeByteRegister(reg, applySet(setBit, bits));
        return OptionalInt.of(8);
    }

    private OptionalInt set(int setBit, CPU cpu) {
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readFromAddress(addr);
        cpu.writeToAddress(addr, applySet(setBit, bits));
        return OptionalInt.of(16);
    }
}
