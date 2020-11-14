package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAG;

public class CYNotSet implements JumpCondition {
    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAG.Cy) == 0;
    }
}
