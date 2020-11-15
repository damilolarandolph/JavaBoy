package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.instructions.jumpconditions.*;

import java.util.OptionalInt;

public class Jump implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
       switch (opcode){
           case 0xc3:
               return jp(cpu);
           case 0xc2:
               return jp(JumpConditions.NZ, cpu);
           case 0xca:
               return jp(JumpConditions.Z, cpu);
           case 0xd2:
               return jp(JumpConditions.NC, cpu);
           case 0xda:
               return jp(JumpConditions.C, cpu);
           case 0x18:
               return jr(cpu);
           case 0x20:
               return jr(JumpConditions.NZ, cpu);
           case 0x28:
               return jr(JumpConditions.Z, cpu);
           case 0x30:
               return jr(JumpConditions.NC, cpu);
           case 0x38:
               return jr(JumpConditions.C, cpu);
           case 0xe9:
               return jpHL(cpu);
           default:
              return OptionalInt.empty();

       }
    }

    private OptionalInt jp(CPU cpu) {
        int byte1 = cpu.readPC();
        int byte2 = cpu.readPC();
        applyJP(byte1, byte2, cpu);
        return OptionalInt.of(16);
    }

    private OptionalInt jp(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jp(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 2);
            return OptionalInt.of(12);
        }
    }

    private OptionalInt jpHL(CPU cpu) {
        int value = cpu.readWordRegister(REGISTERS.H, REGISTERS.L);
        cpu.setPC(value);
        return OptionalInt.of(4);
    }

    private OptionalInt jr(CPU cpu) {
        int value = cpu.readWordPC();
        applyJR(value, cpu);
        return OptionalInt.of(12);
    }

    private OptionalInt jr(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jp(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 1);
            return OptionalInt.of(8);
        }
    }

    private void applyJP(int byte1, int byte2, CPU cpu) {
        int word = ((byte2 << 8) | byte1) ;
        cpu.setPC(word);
    }

    private void applyJR(int value, CPU cpu) {
        byte signedByte = (byte) value;
        cpu.setPC(cpu.getPC() + signedByte );

    }
}

