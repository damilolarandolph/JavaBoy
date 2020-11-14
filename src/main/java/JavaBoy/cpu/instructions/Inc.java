package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Inc implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0x3c:
                return inc(REGISTER.A, cpu);
            case 0x04:
                return inc(REGISTER.B, cpu);
            case 0x0c:
                return inc(REGISTER.C, cpu);
            case 0x14:
                return inc(REGISTER.D, cpu);
            case 0x1c:
                return inc(REGISTER.E, cpu);
            case 0x24:
                return inc(REGISTER.H, cpu);
            case 0x2c:
                return inc(REGISTER.L, cpu);
            case 0x34:
                return inc(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
                //16 bit increment instructions
            case 0x03:
                return inc16(new RegisterPair(REGISTER.B, REGISTER.C), cpu);
            case 0x13:
                return inc16(new RegisterPair(REGISTER.D, REGISTER.E), cpu);
            case 0x23:
                return inc16(new RegisterPair(REGISTER.H, REGISTER.L),cpu);
            case 0x33:
                return inc16SP(cpu);


            default:
                return OptionalInt.empty();
        }
    }

    private OptionalInt inc(REGISTER reg, CPU cpu) {
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

        cpu.writeWordRegister(REGISTER.SP, cpu.readWordRegister(REGISTER.SP) + 1);
        return OptionalInt.of(8);
    }



    private int applyInc(int val, CPU cpu) {
        int result = val + 1;

        if (result == 0)
            cpu.setFlag(FLAG.Z);
        else
            cpu.resetFlag(FLAG.Z);

        boolean halfCarry = (((val & 0xf) + (0x01)) & 0x10) == 0x10;
        if (halfCarry)
            cpu.setFlag(FLAG.H);
        else
            cpu.resetFlag(FLAG.H);

        cpu.resetFlag(FLAG.N);

        return result;
    }
}
