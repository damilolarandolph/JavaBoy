package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.*;

import java.util.OptionalInt;

public class Call implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0xcd:
                return call(cpu);
            case 0xc4:
                return call(new ZNotSet(), cpu);
            case 0xcc:
                return call(new ZSet(), cpu);
            case 0xd4:
                return call(new CYNotSet(), cpu);
            case 0xdc:
                return call(new CYSet(), cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt  call(CPU cpu){
        applyCall(cpu);
        return OptionalInt.of(6);
    }

    private OptionalInt call(JumpCondition condition, CPU cpu){
        if (condition.test(cpu)){
            return call(cpu);
        }else
        {
            cpu.setPC(cpu.getPC() + 2);
            return OptionalInt.of(3);
        }
    }

    private void applyCall(CPU cpu) {
        cpu.decrementSP();
        Address addr = new Address(cpu.getSP());
        cpu.decrementSP();
        Address addrlow = new Address(cpu.getSP());
        cpu.writeToAddress(addr, cpu.getPC() >>> 8);
        cpu.writeToAddress(addrlow, cpu.getPC() & 0xff);
        int word = cpu.readWordFromPC();
        cpu.setPC(word);

    }
}
