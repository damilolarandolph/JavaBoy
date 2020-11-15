package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

import java.util.OptionalInt;

public interface Instruction {
    OptionalInt execute(int opcode, CPU cpu);
}
