package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

public interface Instruction {
    boolean execute(int opcode, CPU cpu);
}
