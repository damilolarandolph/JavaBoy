package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.registers.RegisterPairs;

public class Reset implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x87:
                return res(0, REGISTERS.A, cpu);
            case 0x80:
                return res(0, REGISTERS.B, cpu);
            case 0x81:
                return res(0, REGISTERS.C, cpu);
            case 0x82:
                return res(0, REGISTERS.D, cpu);
            case 0x83:
                return res(0, REGISTERS.E, cpu);
            case 0x84:
                return res(0, REGISTERS.H, cpu);
            case 0x85:
                return res(0, REGISTERS.L, cpu);
            case 0x8f:
                return res(1, REGISTERS.A, cpu);
            case 0x88:
                return res(1, REGISTERS.B, cpu);
            case 0x89:
                return res(1, REGISTERS.C, cpu);
            case 0x8a:
                return res(1, REGISTERS.D, cpu);
            case 0x8b:
                return res(1, REGISTERS.E, cpu);
            case 0x8c:
                return res(1, REGISTERS.H, cpu);
            case 0x8d:
                return res(1, REGISTERS.L, cpu);
            case 0x97:
                return res(2, REGISTERS.A, cpu);
            case 0x90:
                return res(2, REGISTERS.B, cpu);
            case 0x91:
                return res(2, REGISTERS.C, cpu);
            case 0x92:
                return res(2, REGISTERS.D, cpu);
            case 0x93:
                return res(2, REGISTERS.E, cpu);
            case 0x94:
                return res(2, REGISTERS.H, cpu);
            case 0x95:
                return res(2, REGISTERS.L, cpu);
            case 0x9f:
                return res(3, REGISTERS.A, cpu);
            case 0x98:
                return res(3, REGISTERS.B, cpu);
            case 0x99:
                return res(3, REGISTERS.C, cpu);
            case 0x9a:
                return res(3, REGISTERS.D, cpu);
            case 0x9b:
                return res(3, REGISTERS.E, cpu);
            case 0x9c:
                return res(3, REGISTERS.H, cpu);
            case 0x9d:
                return res(3, REGISTERS.L, cpu);
            case 0xa7:
                return res(4, REGISTERS.A, cpu);
            case 0xa0:
                return res(4, REGISTERS.B, cpu);
            case 0xa1:
                return res(4, REGISTERS.C, cpu);
            case 0xa2:
                return res(4, REGISTERS.D, cpu);
            case 0xa3:
                return res(4, REGISTERS.E, cpu);
            case 0xa4:
                return res(4, REGISTERS.H, cpu);
            case 0xa5:
                return res(4, REGISTERS.L, cpu);
            case 0xaf:
                return res(5, REGISTERS.A, cpu);
            case 0xa8:
                return res(5, REGISTERS.B, cpu);
            case 0xa9:
                return res(5, REGISTERS.C, cpu);
            case 0xaa:
                return res(5, REGISTERS.D, cpu);
            case 0xab:
                return res(5, REGISTERS.E, cpu);
            case 0xac:
                return res(5, REGISTERS.H, cpu);
            case 0xad:
                return res(5, REGISTERS.L, cpu);
            case 0xb7:
                return res(6, REGISTERS.A, cpu);
            case 0xb0:
                return res(6, REGISTERS.B, cpu);
            case 0xb1:
                return res(6, REGISTERS.C, cpu);
            case 0xb2:
                return res(6, REGISTERS.D, cpu);
            case 0xb3:
                return res(6, REGISTERS.E, cpu);
            case 0xb4:
                return res(6, REGISTERS.H, cpu);
            case 0xb5:
                return res(6, REGISTERS.L, cpu);
            case 0xbf:
                return res(7, REGISTERS.A, cpu);
            case 0xb8:
                return res(7, REGISTERS.B, cpu);
            case 0xb9:
                return res(7, REGISTERS.C, cpu);
            case 0xba:
                return res(7, REGISTERS.D, cpu);
            case 0xbb:
                return res(7, REGISTERS.E, cpu);
            case 0xbc:
                return res(7, REGISTERS.H, cpu);
            case 0xbd:
                return res(7, REGISTERS.L, cpu);
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
                return false;
        }
    }

    private boolean res(int resetBit, REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        cpu.writeRegister(reg, applyRes(resetBit, bits));
        cpu.addCycles(2);
        return true;
    }

    private boolean res(int resetBit, CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        cpu.writeAddress(addr, applyRes(resetBit, bits));
        cpu.addCycles(4);
        return true;
    }

    private int applyRes(int resetBit, int value) {
        int restBitMask = 0xfe << resetBit;
        return value & restBitMask;
    }


}

