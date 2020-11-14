package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

public class ZSet implements JumpCondition {

    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAGS.Z) == 1;
    }
}
