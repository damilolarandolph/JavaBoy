package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Sub implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x97:
                return sub(REGISTER.A, REGISTER.A, cpu);
            case 0x90:
                return sub(REGISTER.A, REGISTER.B, cpu);
            case 0x91:
                return  sub(REGISTER.A , REGISTER.C, cpu);
            case 0x92:
                return sub(REGISTER.A, REGISTER.D, cpu);
            case 0x93:
                return sub(REGISTER.A, REGISTER.E, cpu);
            case 0x94:
                return sub(REGISTER.A, REGISTER.H, cpu);
            case 0x95:
                return  sub(REGISTER.A, REGISTER.L, cpu);
            case 0x96:
                return sub(REGISTER.A, new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0xd6:
                return sub(REGISTER.A, cpu);
            case 0x9f:
                return sbc(REGISTER.A, REGISTER.A, cpu);
            case 0x98:
                return sbc(REGISTER.A, REGISTER.B, cpu);
            case 0x99:
                return sbc(REGISTER.A, REGISTER.C, cpu);
            case 0x9a:
                return sbc(REGISTER.A, REGISTER.D, cpu);
            case 0x9b:
                return sbc(REGISTER.A, REGISTER.E, cpu);
            case 0x9c:
                return sbc(REGISTER.A, REGISTER.H, cpu);
            case 0x9d:
                return sbc(REGISTER.A, REGISTER.L, cpu);
            case 0x9e:
                return  sbc(REGISTER.A, new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case  0xde:
                return sbc(REGISTER.A, cpu);

            default:
                return OptionalInt.empty();
        }
    }


   private OptionalInt sub(REGISTER reg1, REGISTER reg2, CPU cpu){
        int val1 = cpu.readByteRegister(reg1);
        int val2 = cpu.readByteRegister(reg2);

        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

  private   OptionalInt sub(REGISTER reg, CPU cpu){
        int val1 = cpu.readByteRegister(reg);
        int val2 = cpu.readByteFromPC();

        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

   private OptionalInt sub(REGISTER reg, RegisterPair pair, CPU cpu){
        int val1 = cpu.readByteRegister(reg);
        int val2 = cpu.readFromAddress(new Address(cpu.readWordRegister(pair)));


        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


  private   OptionalInt sbc(REGISTER reg1, REGISTER reg2, CPU cpu){
        int val1 = cpu.readByteRegister(reg1);
        int val2 = cpu.readByteRegister(reg2) + cpu.getFlag(FLAG.Cy);

        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

   private OptionalInt sbc(REGISTER reg1,  CPU cpu){
        int val1 = cpu.readByteRegister(reg1);
        int val2 = cpu.readByteFromPC() + cpu.getFlag(FLAG.Cy);

        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

  private   OptionalInt sbc(REGISTER reg1,  RegisterPair pair, CPU cpu){

        int val1 = cpu.readByteRegister(reg1);
        int val2 = cpu.readFromAddress(new Address(cpu.readWordRegister(pair))) + cpu.getFlag(FLAG.Cy);

        cpu.writeByteRegister(REGISTER.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


  private   int subBytes(int val1, int val2, CPU cpu) {

        int result = (val1 & 0xff) - (val2 & 0xff);

        if (result == 0) {
            cpu.setFlag(FLAG.Z);
        } else {
            cpu.resetFlag(FLAG.Z);
        }
        boolean borrowCheck = (val1 & 0xf) < (val2 & 0xf) ;
        if (borrowCheck) {
            cpu.setFlag(FLAG.H);
            cpu.setFlag(FLAG.Cy);
        } else {
            cpu.resetFlag(FLAG.H);
            cpu.resetFlag(FLAG.Cy);
        }

        cpu.setFlag(FLAG.N);


        return result;


    }
}
