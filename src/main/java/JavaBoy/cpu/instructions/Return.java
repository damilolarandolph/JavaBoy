package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.*;

import java.util.OptionalInt;

public class Return implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
       switch(opcode){
           case 0xc9:
               return ret(cpu);
           case 0xd9:
               return reti(cpu);
           case 0xc0:
               return ret(new ZNotSet(), cpu);
           case 0xc8:
               return ret(new ZSet(), cpu);
           case 0xd0:
               return ret(new CYNotSet(), cpu);
           case 0xd8:
               return ret(new CYSet(),cpu);
           case 0xc7:
               return rst(0x0, cpu);
           case 0xcf:
               return rst(0x08, cpu);
           case 0xd7:
               return rst(0x10, cpu);
           case 0xdf:
               return rst(0x18, cpu);
           case 0xe7:
               return rst(0x20, cpu);
           case 0xef:
               return rst(0x28, cpu);
           case 0xf7:
               return rst(0x30, cpu);
           case 0xff:
               return rst(0x38, cpu);
           default:
               return OptionalInt.empty();
       }
    }

    private void applyRet(CPU cpu){
        int lowByte = cpu.readFromAddress(new Address(cpu.popSP()));
        int highByte = cpu.readFromAddress(new Address(cpu.popSP()));

        cpu.setPC((highByte << 8) | lowByte);

    }

    private OptionalInt ret(CPU cpu){
        applyRet(cpu);
        return OptionalInt.of(4);
    }

    private OptionalInt ret(JumpCondition condition, CPU cpu){
        if(condition.test(cpu)){
            applyRet(cpu);
            return OptionalInt.of(5);
        }else{
            cpu.setPC(cpu.getPC() + 1);
            return OptionalInt.of(2);
        }
    }

    private OptionalInt reti(CPU cpu){
        applyRet(cpu);
        cpu.enableInterrupts();
        return  OptionalInt.of(4);
    }


    private OptionalInt rst(int lowP, CPU cpu){
        cpu.pushSP(cpu.getPC() >>> 8);
        cpu.pushSP(cpu.getPC() & 0xff);

        cpu.setPC(lowP);


        return OptionalInt.of(4);
    }

}



