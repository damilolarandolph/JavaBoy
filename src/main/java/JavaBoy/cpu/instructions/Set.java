package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.registers.RegisterPairs;

public class Set implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xc7:
                return set(0, REGISTERS.A, cpu);
            case 0xc0:
                return set(0, REGISTERS.B, cpu);
            case 0xc1:
                return set(0, REGISTERS.C, cpu);
            case 0xc2:
                return set(0, REGISTERS.D, cpu);
            case 0xc3:
                return set(0, REGISTERS.E, cpu);
            case 0xc4:
                return set(0, REGISTERS.H, cpu);
            case 0xc5:
                return set(0, REGISTERS.L, cpu);
            case 0xcf:
                return set(1, REGISTERS.A, cpu);
            case 0xc8:
                return set(1, REGISTERS.B, cpu);
            case 0xc9:
                return set(1, REGISTERS.C, cpu);
            case 0xca:
                return set(1, REGISTERS.D, cpu);
            case 0xcb:
                return set(1, REGISTERS.E, cpu);
            case 0xcc:
                return set(1, REGISTERS.H, cpu);
            case 0xcd:
                return set(1, REGISTERS.L, cpu);
            case 0xd7:
                return set(2, REGISTERS.A, cpu);
            case 0xd0:
                return set(2, REGISTERS.B, cpu);
            case 0xd1:
                return set(2, REGISTERS.C, cpu);
            case 0xd2:
                return set(2, REGISTERS.D, cpu);
            case 0xd3:
                return set(2, REGISTERS.E, cpu);
            case 0xd4:
                return set(2, REGISTERS.H, cpu);
            case 0xd5:
                return set(2, REGISTERS.L, cpu);
            case 0xdf:
                return set(3, REGISTERS.A, cpu);
            case 0xd8:
                return set(3, REGISTERS.B, cpu);
            case 0xd9:
                return set(3, REGISTERS.C, cpu);
            case 0xda:
                return set(3, REGISTERS.D, cpu);
            case 0xdb:
                return set(3, REGISTERS.E, cpu);
            case 0xdc:
                return set(3, REGISTERS.H, cpu);
            case 0xdd:
                return set(3, REGISTERS.L, cpu);
            case 0xe7:
                return set(4, REGISTERS.A, cpu);
            case 0xe0:
                return set(4, REGISTERS.B, cpu);
            case 0xe1:
                return set(4, REGISTERS.C, cpu);
            case 0xe2:
                return set(4, REGISTERS.D, cpu);
            case 0xe3:
                return set(4, REGISTERS.E, cpu);
            case 0xe4:
                return set(4, REGISTERS.H, cpu);
            case 0xe5:
                return set(4, REGISTERS.L, cpu);
            case 0xef:
                return set(5, REGISTERS.A, cpu);
            case 0xe8:
                return set(5, REGISTERS.B, cpu);
            case 0xe9:
                return set(5, REGISTERS.C, cpu);
            case 0xea:
                return set(5, REGISTERS.D, cpu);
            case 0xeb:
                return set(5, REGISTERS.E, cpu);
            case 0xec:
                return set(5, REGISTERS.H, cpu);
            case 0xed:
                return set(5, REGISTERS.L, cpu);
            case 0xf7:
                return set(6, REGISTERS.A, cpu);
            case 0xf0:
                return set(6, REGISTERS.B, cpu);
            case 0xf1:
                return set(6, REGISTERS.C, cpu);
            case 0xf2:
                return set(6, REGISTERS.D, cpu);
            case 0xf3:
                return set(6, REGISTERS.E, cpu);
            case 0xf4:
                return set(6, REGISTERS.H, cpu);
            case 0xf5:
                return set(6, REGISTERS.L, cpu);
            case 0xff:
                return set(7, REGISTERS.A, cpu);
            case 0xf8:
                return set(7, REGISTERS.B, cpu);
            case 0xf9:
                return set(7, REGISTERS.C, cpu);
            case 0xfa:
                return set(7, REGISTERS.D, cpu);
            case 0xfb:
                return set(7, REGISTERS.E, cpu);
            case 0xfc:
                return set(7, REGISTERS.H, cpu);
            case 0xfd:
                return set(7, REGISTERS.L, cpu);
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
                return false;
        }
    }


    private int applySet(int setBit, int value) {
        int setBitFlip = 0x01 << setBit;
        return setBitFlip | value;
    }

    private boolean set(int setBit, REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        cpu.writeRegister(reg, applySet(setBit, bits));
        cpu.addCycles(2);
        return true;
    }

    private boolean set(int setBit, CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        cpu.writeAddress(addr, applySet(setBit, bits));
        cpu.addCycles(4);
        return true;
    }
}
