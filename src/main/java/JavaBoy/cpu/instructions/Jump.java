package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTER;
import JavaBoy.cpu.instructions.jumpconditions.*;

import java.util.OptionalInt;

public class Jump implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
       switch (opcode){
           case 0xc3:
               return jp(cpu);
           case 0xc2:
               return jp(new ZNotSet(), cpu);
           case 0xca:
               return jp(new ZSet(), cpu);
           case 0xd2:
               return jp(new CYNotSet(), cpu);
           case 0xda:
               return jp(new CYSet(), cpu);
           case 0x18:
               return jr(cpu);
           case 0x20:
               return jr(new ZNotSet(), cpu);
           case 0x28:
               return jr(new ZSet(), cpu);
           case 0x30:
               return jr(new CYNotSet(), cpu);
           case 0x38:
               return jr(new CYSet(), cpu);
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
        return OptionalInt.of(4);
    }

    private OptionalInt jp(JumpCondition condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jp(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 2);
            return OptionalInt.of(3);
        }
    }

    private OptionalInt jpHL(CPU cpu) {
        int value = cpu.readWordRegister(REGISTER.H, REGISTER.L);
        cpu.setPC(value);
        return OptionalInt.of(1);
    }

    private OptionalInt jr(CPU cpu) {
        int value = cpu.readWordPC();
        applyJR(value, cpu);
        return OptionalInt.of(3);
    }

    private OptionalInt jr(JumpCondition condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jp(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 1);
            return OptionalInt.of(2);
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

