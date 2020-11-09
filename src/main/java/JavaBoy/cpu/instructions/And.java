package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class And implements Instruction {


    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0xa7:
                return and(REGISTER.A, cpu);
            case 0xa0:
                return and(REGISTER.B, cpu);
            case 0xa1:
                return and(REGISTER.C, cpu);
            case 0xa2:
                return and(REGISTER.D, cpu);
            case 0xa3:
                return and(REGISTER.E, cpu);
            case 0xa4:
                return and(REGISTER.H, cpu);
            case 0xa5:
                return and(REGISTER.L, cpu);
            case 0xa6:
                return and(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0xe6:
                return and(cpu);
            default:
                return OptionalInt.empty();
        }
    }


    OptionalInt and(REGISTER reg, CPU cpu){

        int reg1 = cpu.readByteRegister(REGISTER.A);
        int reg2 = cpu.readByteRegister(reg);

        cpu.writeByteRegister(REGISTER.A, applyAnd(reg1, reg2, cpu));

        return OptionalInt.of(4);
    }

    OptionalInt and(CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readByteFromPC();

        cpu.writeByteRegister(REGISTER.A, applyAnd(val1, val2, cpu));

        return OptionalInt.of(8);
    }

    OptionalInt and(RegisterPair pair, CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readFromAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeByteRegister(REGISTER.A, applyAnd(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    int applyAnd(int val1, int val2, CPU cpu){
        int result = (val1 & 0xff) & (val2 & 0xff);

        if (result == 0x0){
           cpu.setFlag(FLAG.Z);
        }else{
            cpu.resetFlag(FLAG.Z);
        }

        cpu.setFlag(FLAG.H);
        cpu.resetFlag(FLAG.Cy);
        cpu.resetFlag(FLAG.N);
        return result;
    }


}
