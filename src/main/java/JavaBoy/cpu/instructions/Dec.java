package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;
import JavaBoy.cpu.flags.FLAGS;

import java.util.OptionalInt;

public class Dec implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case  0x3d:
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
                return dec(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0x0b:
                return dec16(new RegisterPair(REGISTERS.B, REGISTERS.C), cpu);
            case 0x1b:
                return dec16(new RegisterPair(REGISTERS.D, REGISTERS.E), cpu);
            case 0x2b:
                return dec16(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0x3b:
                return dec16SP(cpu);
            default:
                return  OptionalInt.empty();
        }
    }



  private OptionalInt dec(REGISTERS reg, CPU cpu){
        int value = cpu.readRegister(reg);

        cpu.writeRegister(reg, applyDec(value, cpu));

        return OptionalInt.of(4);
   }

   private OptionalInt dec(RegisterPair pair, CPU cpu){
        Address address = new Address(cpu.readWordRegister(pair));
        int value = cpu.readAddress(address);

        cpu.writeAddress(address, applyDec(value, cpu));

        return OptionalInt.of(12);
   }

   private OptionalInt dec16(RegisterPair pair, CPU cpu){

        cpu.writeWordRegister(pair, cpu.readWordRegister(pair) + 1);
        return OptionalInt.of(8);
   }

   private OptionalInt dec16SP(CPU cpu){
        cpu.writeWordRegister(REGISTERS.SP, cpu.readWordRegister(REGISTERS.SP) + 1);
        return OptionalInt.of(8);
   }

   private int applyDec(int value, CPU cpu) {
        int result = value - 0x01;

        if (result == 0x0)
            cpu.setFlag(FLAGS.Z);
        else
            cpu.resetFlag(FLAGS.Z);

        if ((value & 0x01) < (0x01))
            cpu.setFlag(FLAGS.H);
        else
            cpu.resetFlag(FLAGS.H);

        cpu.setFlag(FLAGS.N);



        return result;

    }
}
