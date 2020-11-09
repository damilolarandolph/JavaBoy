package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Or implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0xb7:
                return or(REGISTER.A, cpu);
            case 0xb0:
                return or(REGISTER.B, cpu);
            case 0xb1:
                return  or(REGISTER.C, cpu);
            case 0xb2:
                return or(REGISTER.D, cpu);
            case 0xb3:
                return or(REGISTER.E, cpu);
            case 0xb4:
                return or(REGISTER.H, cpu);
            case 0xb5:
                return or(REGISTER.L, cpu);
            case 0xb6:
                return or(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0xf6:
                return or(cpu);

            default:
                return OptionalInt.empty();
        }
    }



    OptionalInt or(REGISTER reg, CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readByteRegister(reg);


        cpu.writeByteRegister(REGISTER.A, applyOr(val1, val2, cpu));


        return OptionalInt.of(4);
    }


    OptionalInt or(RegisterPair pair, CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readFromAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeByteRegister(REGISTER.A, applyOr(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    OptionalInt or(CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readByteFromPC();

        cpu.writeByteRegister(REGISTER.A, applyOr(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    int applyOr(int val1, int val2, CPU cpu){
        int result = (val1 & 0xff) | (val2 & 0xff);

        if (result <= 0x0){
          cpu.setFlag(FLAG.Z);
        }else{
            cpu.resetFlag(FLAG.Z);
        }

        cpu.resetFlag(FLAG.Cy);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        return result;
    }
}
