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
                return call(JumpConditions.NZ, cpu);
            case 0xcc:
                return call(JumpConditions.Z, cpu);
            case 0xd4:
                return call(JumpConditions.NC, cpu);
            case 0xdc:
                return call(JumpConditions.C, cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt  call(CPU cpu){
        applyCall(cpu);
        return OptionalInt.of(24);
    }

    private OptionalInt call(JumpConditions condition, CPU cpu){
        if (condition.test(cpu)){
            return call(cpu);
        }else
        {
            cpu.setPC(cpu.getPC() + 2);
            return OptionalInt.of(12);
        }
    }

    private void applyCall(CPU cpu) {
        cpu.decrementSP();
        Address addr = new Address(cpu.getSP());
        cpu.decrementSP();
        Address addrlow = new Address(cpu.getSP());
        cpu.writeAddress(addr, cpu.getPC() >>> 8);
        cpu.writeAddress(addrlow, cpu.getPC() & 0xff);
        int word = cpu.readWordPC();
        cpu.setPC(word);

    }
}
