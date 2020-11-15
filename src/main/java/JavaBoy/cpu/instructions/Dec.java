package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Dec implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x3d:
                return dec(REGISTERS.A, cpu);
            case 0x05:
                return dec(REGISTERS.B, cpu);
            case 0x0d:
                return dec(REGISTERS.C, cpu);
            case 0x15:
                return dec(REGISTERS.D, cpu);
            case 0x1d:
                return dec(REGISTERS.E, cpu);
            case 0x25:
                return dec(REGISTERS.H, cpu);
            case 0x2d:
                return dec(REGISTERS.L, cpu);
            case 0x35:
                return decHL(cpu);
            case 0x0b:
                return dec16(RegisterPairs.BC, cpu);
            case 0x1b:
                return dec16(RegisterPairs.DE, cpu);
            case 0x2b:
                return dec16(RegisterPairs.HL, cpu);
            case 0x3b:
                return dec16SP(cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt dec(REGISTERS reg, CPU cpu) {
        int value = cpu.readRegister(reg);

        cpu.writeRegister(reg, applyDec(value, cpu));

        return OptionalInt.of(4);
    }

    private OptionalInt decHL(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readAddress(address);

        cpu.writeAddress(address, applyDec(value, cpu));

        return OptionalInt.of(12);
    }

    private OptionalInt dec16(RegisterPairs pair, CPU cpu) {

        cpu.writeWordRegister(pair, cpu.readWordRegister(pair) - 1);
        return OptionalInt.of(8);
    }

    private OptionalInt dec16SP(CPU cpu) {
        cpu.setSP(cpu.getSP() - 1);
        return OptionalInt.of(8);
    }

    private int applyDec(int value, CPU cpu) {
        int result = value - 0x01;
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, (value & 0xf) < 0x01);
        cpu.setFlag(FLAGS.N, true);

        return result;

    }
}
