package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

public class CYSet implements JumpCondition {
    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAGS.Cy) == 1;
    }
}
