package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

public class Nop implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {

        if (opcode == 0x0) {
            cpu.addCycles();
            return true;
        } else {
            return false;
        }

    }
}
