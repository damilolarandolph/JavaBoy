package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Or implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xb7:
                return or(REGISTERS.A, cpu);
            case 0xb0:
                return or(REGISTERS.B, cpu);
            case 0xb1:
                return or(REGISTERS.C, cpu);
            case 0xb2:
                return or(REGISTERS.D, cpu);
            case 0xb3:
                return or(REGISTERS.E, cpu);
            case 0xb4:
                return or(REGISTERS.H, cpu);
            case 0xb5:
                return or(REGISTERS.L, cpu);
            case 0xb6:
                return orHL(cpu);
            case 0xf6:
                return or(cpu);

            default:
                return OptionalInt.empty();
        }
    }


    OptionalInt or(REGISTERS reg, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg);
        cpu.writeRegister(REGISTERS.A, applyOr(val1, val2, cpu));
        return OptionalInt.of(4);
    }


    OptionalInt orHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));
        cpu.writeRegister(REGISTERS.A, applyOr(val1, val2, cpu));
        return OptionalInt.of(8);
    }


    OptionalInt or(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();
        cpu.writeRegister(REGISTERS.A, applyOr(val1, val2, cpu));
        return OptionalInt.of(8);
    }


    int applyOr(int val1, int val2, CPU cpu) {
        int result = (val1 & 0xff) | (val2 & 0xff);
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.C, false);
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        return result;
    }
}
