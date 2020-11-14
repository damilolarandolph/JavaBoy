package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;
import JavaBoy.cpu.flags.FLAG;

import java.util.OptionalInt;

public class Cp implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
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
                return cp(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0xfe:
                return cp(cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt cp(REGISTERS reg, CPU cpu){
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg);

        applyCp(val1, val2 ,cpu);

        return OptionalInt.of(4);
    }


    private OptionalInt cp(RegisterPair pair, CPU cpu){
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));

        applyCp(val1, val2, cpu);

        return OptionalInt.of(8);
    }

    private OptionalInt cp(CPU cpu){
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        applyCp(val1, val2, cpu);

        return OptionalInt.of(8);
    }


    private void applyCp(int val1, int val2, CPU cpu){
        boolean isEqual = (val1 & 0xff) == (val1 & 0xff);

        if (isEqual)
            cpu.setFlag(FLAG.Z);
        else
            cpu.resetFlag(FLAG.Z);

        if((val1 & 0xf) < (val1 & 0xf))
            cpu.setFlag(FLAG.H);
        else
            cpu.resetFlag(FLAG.H);

        if (val1 < val2)
            cpu.setFlag(FLAG.Cy);
        else
            cpu.resetFlag(FLAG.Cy);


        cpu.resetFlag(FLAG.N);

    }
}





