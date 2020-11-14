package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class Reset implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0x87:
                return res(0, REGISTER.A, cpu);
            case 0x80:
                return res(0, REGISTER.B, cpu);
            case 0x81:
                return res(0, REGISTER.C, cpu);
            case 0x82:
                return res(0, REGISTER.D, cpu);
            case 0x83:
                return res(0, REGISTER.E, cpu);
            case 0x84:
                return res(0, REGISTER.H, cpu);
            case 0x85:
                return res(0, REGISTER.L, cpu);
            case 0x8f:
                return res(1, REGISTER.A, cpu);
            case 0x88:
                return res(1, REGISTER.B, cpu);
            case 0x89:
                return res(1, REGISTER.C, cpu);
            case 0x8a:
                return res(1, REGISTER.D, cpu);
            case 0x8b:
                return res(1, REGISTER.E, cpu);
            case 0x8c:
                return res(1, REGISTER.H, cpu);
            case 0x8d:
                return res(1, REGISTER.L, cpu);
            case 0x97:
                return res(2, REGISTER.A, cpu);
            case 0x90:
                return res(2, REGISTER.B, cpu);
            case 0x91:
                return res(2, REGISTER.C, cpu);
            case 0x92:
                return res(2, REGISTER.D, cpu);
            case 0x93:
                return res(2, REGISTER.E, cpu);
            case 0x94:
                return res(2, REGISTER.H, cpu);
            case 0x95:
                return res(2, REGISTER.L, cpu);
            case 0x9f:
                return res(3, REGISTER.A, cpu);
            case 0x98:
                return res(3, REGISTER.B, cpu);
            case 0x99:
                return res(3, REGISTER.C, cpu);
            case 0x9a:
                return res(3, REGISTER.D, cpu);
            case 0x9b:
                return res(3, REGISTER.E, cpu);
            case 0x9c:
                return res(3, REGISTER.H, cpu);
            case 0x9d:
                return res(3, REGISTER.L, cpu);
            case 0xa7:
                return res(4, REGISTER.A, cpu);
            case 0xa0:
                return res(4, REGISTER.B, cpu);
            case 0xa1:
                return res(4, REGISTER.C, cpu);
            case 0xa2:
                return res(4, REGISTER.D, cpu);
            case 0xa3:
                return res(4, REGISTER.E, cpu);
            case 0xa4:
                return res(4, REGISTER.H, cpu);
            case 0xa5:
                return res(4, REGISTER.L, cpu);
            case 0xaf:
                return res(5, REGISTER.A, cpu);
            case 0xa8:
                return res(5, REGISTER.B, cpu);
            case 0xa9:
                return res(5, REGISTER.C, cpu);
            case 0xaa:
                return res(5, REGISTER.D, cpu);
            case 0xab:
                return res(5, REGISTER.E, cpu);
            case 0xac:
                return res(5, REGISTER.H, cpu);
            case 0xad:
                return res(5, REGISTER.L, cpu);
            case 0xb7:
                return res(6, REGISTER.A, cpu);
            case 0xb0:
                return res(6, REGISTER.B, cpu);
            case 0xb1:
                return res(6, REGISTER.C, cpu);
            case 0xb2:
                return res(6, REGISTER.D, cpu);
            case 0xb3:
                return res(6, REGISTER.E, cpu);
            case 0xb4:
                return res(6, REGISTER.H, cpu);
            case 0xb5:
                return res(6, REGISTER.L, cpu);
            case 0xbf:
                return res(7, REGISTER.A, cpu);
            case 0xb8:
                return res(7, REGISTER.B, cpu);
            case 0xb9:
                return res(7, REGISTER.C, cpu);
            case 0xba:
                return res(7, REGISTER.D, cpu);
            case 0xbb:
                return res(7, REGISTER.E, cpu);
            case 0xbc:
                return res(7, REGISTER.H, cpu);
            case 0xbd:
                return res(7, REGISTER.L, cpu);
            case 0x86:
                return res(0, cpu);
            case 0x8e:
                return res(1, cpu);
            case 0x96:
                return res(2, cpu);
            case 0x9e:
                return res(3, cpu);
            case 0xa6:
                return res(4, cpu);
            case 0xae:
                return res(5, cpu);
            case 0xb6:
                return res(6, cpu);
            case 0xbe:
                return res(7, cpu);
            default:
                return OptionalInt.empty();
        }
    }

    private OptionalInt res(int resetBit, REGISTER reg, CPU cpu){
        int bits = cpu.readRegister(reg);
        cpu.writeRegister(reg, applyRes(resetBit, bits));
        return OptionalInt.of(8);
    }
    private OptionalInt res(int resetBit, CPU cpu){
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readAddress(addr);
        cpu.writeAddress(addr, applyRes(resetBit, bits));
        return OptionalInt.of(16);
    }

    private int applyRes(int resetBit, int value){
        int restBitMask = 0xfe << resetBit;
        return value & restBitMask;
    }


}

