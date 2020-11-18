package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

public class CCF implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0x3f) {
            cpu.setFlag(FLAGS.C, !cpu.isFlag(FLAGS.C));
            cpu.addCycles();
            return true;
        } else {
            return false;
        }
    }
}
