package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Dec implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case  0x3d:
                return dec(REGISTER.A, cpu);
            case 0x05:
                return dec(REGISTER.B, cpu);
            case 0x0d:
                return dec(REGISTER.C, cpu);
            case 0x15:
                return dec(REGISTER.D, cpu);
            case 0x1d:
                return dec(REGISTER.E, cpu);
            case 0x25:
                return dec(REGISTER.H, cpu);
            case 0x2d:
                return dec(REGISTER.L, cpu);
            case 0x35:
                return dec(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            default:
                return  OptionalInt.empty();
        }
    }



  private OptionalInt dec(REGISTER reg, CPU cpu){
        int value = cpu.readByteRegister(reg);

        cpu.writeByteRegister(reg, applyDec(value, cpu));

        return OptionalInt.of(4);
   }

   private OptionalInt dec(RegisterPair pair, CPU cpu){
        Address address = new Address(cpu.readWordRegister(pair));
        int value = cpu.readFromAddress(address);

        cpu.writeToAddress(address, applyDec(value, cpu));

        return OptionalInt.of(12);
   }

   private int applyDec(int value, CPU cpu) {
        int result = value - 0x01;

        if (result == 0x0)
            cpu.setFlag(FLAG.Z);
        else
            cpu.resetFlag(FLAG.Z);

        if ((value & 0x01) < (0x01))
            cpu.setFlag(FLAG.H);
        else
            cpu.resetFlag(FLAG.H);

        cpu.setFlag(FLAG.N);



        return result;

    }
}
