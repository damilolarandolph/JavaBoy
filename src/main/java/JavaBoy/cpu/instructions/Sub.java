package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;
import JavaBoy.cpu.flags.FLAG;

import java.util.OptionalInt;

public class Sub implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x97:
                return sub(REGISTERS.A, REGISTERS.A, cpu);
            case 0x90:
                return sub(REGISTERS.A, REGISTERS.B, cpu);
            case 0x91:
                return  sub(REGISTERS.A , REGISTERS.C, cpu);
            case 0x92:
                return sub(REGISTERS.A, REGISTERS.D, cpu);
            case 0x93:
                return sub(REGISTERS.A, REGISTERS.E, cpu);
            case 0x94:
                return sub(REGISTERS.A, REGISTERS.H, cpu);
            case 0x95:
                return  sub(REGISTERS.A, REGISTERS.L, cpu);
            case 0x96:
                return sub(REGISTERS.A, new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0xd6:
                return sub(REGISTERS.A, cpu);
            case 0x9f:
                return sbc(REGISTERS.A, REGISTERS.A, cpu);
            case 0x98:
                return sbc(REGISTERS.A, REGISTERS.B, cpu);
            case 0x99:
                return sbc(REGISTERS.A, REGISTERS.C, cpu);
            case 0x9a:
                return sbc(REGISTERS.A, REGISTERS.D, cpu);
            case 0x9b:
                return sbc(REGISTERS.A, REGISTERS.E, cpu);
            case 0x9c:
                return sbc(REGISTERS.A, REGISTERS.H, cpu);
            case 0x9d:
                return sbc(REGISTERS.A, REGISTERS.L, cpu);
            case 0x9e:
                return  sbc(REGISTERS.A, new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case  0xde:
                return sbc(REGISTERS.A, cpu);

            default:
                return OptionalInt.empty();
        }
    }


   private OptionalInt sub(REGISTERS reg1, REGISTERS reg2, CPU cpu){
        int val1 = cpu.readRegister(reg1);
        int val2 = cpu.readRegister(reg2);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

  private   OptionalInt sub(REGISTERS reg, CPU cpu){
        int val1 = cpu.readRegister(reg);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

   private OptionalInt sub(REGISTERS reg, RegisterPair pair, CPU cpu){
        int val1 = cpu.readRegister(reg);
        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));


        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


  private   OptionalInt sbc(REGISTERS reg1, REGISTERS reg2, CPU cpu){
        int val1 = cpu.readRegister(reg1);
        int val2 = cpu.readRegister(reg2) + cpu.getFlag(FLAG.Cy);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(4);
    }

   private OptionalInt sbc(REGISTERS reg1, CPU cpu){
        int val1 = cpu.readRegister(reg1);
        int val2 = cpu.readPC() + cpu.getFlag(FLAG.Cy);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }

  private   OptionalInt sbc(REGISTERS reg1, RegisterPair pair, CPU cpu){

        int val1 = cpu.readRegister(reg1);
        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair))) + cpu.getFlag(FLAG.Cy);

        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));

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
