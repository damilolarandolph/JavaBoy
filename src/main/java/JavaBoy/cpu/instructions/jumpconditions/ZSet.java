package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;

public class ZSet implements JumpCondition {

    @Override
    public boolean test(CPU cpu) {
        return cpu.getFlag(FLAG.Z) == 1;
    }
}
