package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

import static JavaBoy.utils.ArithmeticUtils.isHalfCarry8;

public class Inc implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x3c:
                return inc(REGISTERS.A, cpu);
            case 0x04:
                return inc(REGISTERS.B, cpu);
            case 0x0c:
                return inc(REGISTERS.C, cpu);
            case 0x14:
                return inc(REGISTERS.D, cpu);
            case 0x1c:
                return inc(REGISTERS.E, cpu);
            case 0x24:
                return inc(REGISTERS.H, cpu);
            case 0x2c:
                return inc(REGISTERS.L, cpu);
            case 0x34:
                return incHL(RegisterPairs.HL, cpu);
            //16 bit increment instructions
            case 0x03:
                return inc16(RegisterPairs.BC, cpu);
            case 0x13:
                return inc16(RegisterPairs.DE, cpu);
            case 0x23:
                return inc16(RegisterPairs.HL, cpu);
            case 0x33:
                return inc16SP(cpu);


            default:
                return OptionalInt.empty();
        }
    }

    private OptionalInt inc(REGISTERS reg, CPU cpu) {
        int value = cpu.readRegister(reg);

        cpu.writeRegister(reg, applyInc(value, cpu));
        return OptionalInt.of(4);
    }

    private OptionalInt incHL(RegisterPairs pair, CPU cpu) {
        int address = cpu.readWordRegister(pair);
        int value = cpu.readAddress(address);
        cpu.writeAddress(address, applyInc(value, cpu));

        return OptionalInt.of(12);

    }

    private OptionalInt inc16(RegisterPairs pair, CPU cpu) {

        cpu.writeWordRegister(pair, cpu.readWordRegister(pair) + 1);
        return OptionalInt.of(16);
    }

    private OptionalInt inc16SP(CPU cpu) {
        cpu.setSP(cpu.getSP() + 1);
        return OptionalInt.of(8);
    }


    private int applyInc(int val, CPU cpu) {
        int result = val + 1;
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, isHalfCarry8(val, 1));
        cpu.setFlag(FLAGS.N, false);

        return result;
    }
}
