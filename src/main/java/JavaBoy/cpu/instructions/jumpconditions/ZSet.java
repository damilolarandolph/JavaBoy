package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAG;

public class ZSet implements JumpCondition {

    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAG.Z) == 1;
    }
}
