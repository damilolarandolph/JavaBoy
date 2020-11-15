package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Xor implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xaf:
                return xor(REGISTERS.A, cpu);
            case 0xa8:
                return xor(REGISTERS.B, cpu);
            case 0xa9:
                return xor(REGISTERS.C, cpu);
            case 0xaa:
                return xor(REGISTERS.D, cpu);
            case 0xab:
                return xor(REGISTERS.E, cpu);
            case 0xac:
                return xor(REGISTERS.H, cpu);
            case 0xad:
                return xor(REGISTERS.L, cpu);
            case 0xae:
                return xorHL(cpu);
            case 0xee:
                return xor(cpu);
            default:
                return OptionalInt.empty();
        }
    }

    private OptionalInt xor(REGISTERS reg, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg);

        cpu.writeRegister(REGISTERS.A, applyXOR(val1, val2, cpu));
        return OptionalInt.of(4);
    }

    private OptionalInt xorHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));

        cpu.writeRegister(REGISTERS.A, applyXOR(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private OptionalInt xor(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();
        cpu.writeRegister(REGISTERS.A, applyXOR(val1, val2, cpu));

        return OptionalInt.of(8);
    }

    private int applyXOR(int val1, int val2, CPU cpu) {
        int result = (val1 & 0xff) ^ (val2 & 0xff);
        cpu.setFlag(FLAGS.Z, result == 0);

        cpu.setFlag(FLAGS.C, false);
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);


        return result;

    }
}

