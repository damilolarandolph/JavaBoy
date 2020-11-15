package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Cp implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xbf:
                return cp(REGISTERS.A, cpu);
            case 0xb8:
                return cp(REGISTERS.B, cpu);
            case 0xb9:
                return cp(REGISTERS.C, cpu);
            case 0xba:
                return cp(REGISTERS.D, cpu);
            case 0xbb:
                return cp(REGISTERS.E, cpu);
            case 0xbc:
                return cp(REGISTERS.H, cpu);
            case 0xbd:
                return cp(REGISTERS.L, cpu);
            case 0xbe:
                return cpHL(cpu);
            case 0xfe:
                return cp(cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt cp(REGISTERS reg, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg);

        applyCp(val1, val2, cpu);

        return OptionalInt.of(4);
    }


    private OptionalInt cpHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));

        applyCp(val1, val2, cpu);

        return OptionalInt.of(8);
    }

    private OptionalInt cp(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        applyCp(val1, val2, cpu);

        return OptionalInt.of(8);
    }


    private void applyCp(int val1, int val2, CPU cpu) {
        boolean isEqual = (val1 & 0xff) == (val1 & 0xff);

        cpu.setFlag(FLAGS.Z, isEqual);
        cpu.setFlag(FLAGS.H, (val1 & 0xf) < (val1 & 0xf));
        cpu.setFlag(FLAGS.C, val1 < val2);
        cpu.setFlag(FLAGS.N, false);

    }
}





