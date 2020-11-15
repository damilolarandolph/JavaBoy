package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

import java.util.OptionalInt;

public class CB implements Instruction {
    final Instruction[] instructions;

    public CB(Instruction[] instructions) {
        this.instructions = instructions;
    }

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0xcb) {
            OptionalInt result;
            int cbOpcode = cpu.readPC();
            for (Instruction instruction : instructions) {
                result = instruction.execute(cbOpcode, cpu);
                if (result.isPresent())
                    return OptionalInt.of(4 + result.hashCode());
            }
        }
        return OptionalInt.empty();
    }
}
