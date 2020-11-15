package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Sub implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x97:
                return sub(REGISTERS.A, cpu);
            case 0x90:
                return sub(REGISTERS.B, cpu);
            case 0x91:
                return sub(REGISTERS.C, cpu);
            case 0x92:
                return sub(REGISTERS.D, cpu);
            case 0x93:
                return sub(REGISTERS.E, cpu);
            case 0x94:
                return sub(REGISTERS.H, cpu);
            case 0x95:
                return sub(REGISTERS.L, cpu);
            case 0x96:
                return subHL(cpu);
            case 0xd6:
                return sub(cpu);
            case 0x9f:
                return sbc(REGISTERS.A, cpu);
            case 0x98:
                return sbc(REGISTERS.B, cpu);
            case 0x99:
                return sbc(REGISTERS.C, cpu);
            case 0x9a:
                return sbc(REGISTERS.D, cpu);
            case 0x9b:
                return sbc(REGISTERS.E, cpu);
            case 0x9c:
                return sbc(REGISTERS.H, cpu);
            case 0x9d:
                return sbc(REGISTERS.L, cpu);
            case 0x9e:
                return sbcHL(cpu);
            case 0xde:
                return sbc(cpu);

            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt sub(REGISTERS reg2, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg2);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

    private OptionalInt sub(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

    private OptionalInt subHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));


        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private OptionalInt sbc(REGISTERS reg2, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg2) + cpu.getFlag(FLAGS.C);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

    private OptionalInt sbc(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC() + cpu.getFlag(FLAGS.C);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

    private OptionalInt sbcHL(CPU cpu) {

        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL) + cpu.getFlag(FLAGS.C));

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private int subBytes(int val1, int val2, CPU cpu) {

        int result = (val1 & 0xff) - (val2 & 0xff);

        cpu.setFlag(FLAGS.Z, result == 0);
        boolean borrowCheck = (val1 & 0xf) < (val2 & 0xf);
        cpu.setFlag(FLAGS.H, borrowCheck);
        cpu.setFlag(FLAGS.C, val1 < val2);
        cpu.setFlag(FLAGS.N, true);


        return result;


    }
}
