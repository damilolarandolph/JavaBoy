package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.JumpConditions;

import java.util.OptionalInt;

import static JavaBoy.utils.BitUtils.getLsb;
import static JavaBoy.utils.BitUtils.getMsb;

public class Call implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
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


    private OptionalInt call(CPU cpu) {
        applyCall(cpu);
        return OptionalInt.of(24);
    }

    private OptionalInt call(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            return call(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 2);
            return OptionalInt.of(12);
        }
    }

    private void applyCall(CPU cpu) {

        int word = cpu.readWordPC();
        cpu.pushSP(getMsb(cpu.getPC()));
        cpu.pushSP(getLsb(cpu.getPC()));
        cpu.setPC(word);

    }
}
