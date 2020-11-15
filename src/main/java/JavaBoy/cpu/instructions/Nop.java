package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

import java.util.OptionalInt;

public class Nop implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {

        if (opcode == 0x0) {
            return OptionalInt.of(4);
        } else {
            return OptionalInt.empty();
        }

    }
}
