package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

 class ZFlagNotSet implements JumpCondition {
    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAGS.Z) == 0;
    }

}