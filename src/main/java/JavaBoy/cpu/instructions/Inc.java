package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;
import JavaBoy.cpu.flags.FLAGS;

import java.util.OptionalInt;

public class Inc implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
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
                return inc(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
                //16 bit increment instructions
            case 0x03:
                return inc16(new RegisterPair(REGISTERS.B, REGISTERS.C), cpu);
            case 0x13:
                return inc16(new RegisterPair(REGISTERS.D, REGISTERS.E), cpu);
            case 0x23:
                return inc16(new RegisterPair(REGISTERS.H, REGISTERS.L),cpu);
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

    private OptionalInt inc(RegisterPair pair, CPU cpu) {
        Address address = new Address(cpu.readWordRegister(pair));

        int value = cpu.readAddress(address);

        cpu.writeAddress(address, applyInc(value, cpu));

        return OptionalInt.of(12);

    }

    private OptionalInt inc16(RegisterPair pair, CPU cpu){

        cpu.writeWordRegister(pair, cpu.readWordRegister(pair) + 1);
        return OptionalInt.of(16);
    }
    private OptionalInt inc16SP(CPU cpu){

        cpu.writeWordRegister(REGISTERS.SP, cpu.readWordRegister(REGISTERS.SP) + 1);
        return OptionalInt.of(8);
    }



    private int applyInc(int val, CPU cpu) {
        int result = val + 1;

        if (result == 0)
            cpu.setFlag(FLAGS.Z);
        else
            cpu.resetFlag(FLAGS.Z);

        boolean halfCarry = (((val & 0xf) + (0x01)) & 0x10) == 0x10;
        if (halfCarry)
            cpu.setFlag(FLAGS.H);
        else
            cpu.resetFlag(FLAGS.H);

        cpu.resetFlag(FLAGS.N);

        return result;
    }
}
