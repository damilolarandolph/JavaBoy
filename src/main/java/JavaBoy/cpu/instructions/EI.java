package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

public class EI implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0xfb) {
            cpu.enableInterrupts();
            cpu.addCycles();
            return true;
        }
        return false;
    }
}
