package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

public class SCF implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0x37) {
            cpu.setFlag(FLAGS.C, true);
            cpu.addCycles();
            return true;
        } else {
            return false;
        }
    }
}
