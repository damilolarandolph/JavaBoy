package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;

interface JumpCondition {

    boolean test(CPU cpu);
}
