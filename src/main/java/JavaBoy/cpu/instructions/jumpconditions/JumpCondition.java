package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;

public interface JumpCondition {

    boolean test(CPU cpu);
}
