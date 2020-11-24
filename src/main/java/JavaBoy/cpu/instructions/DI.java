package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

public class DI implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0xf3) {
            cpu.disableInterrupts();
            cpu.addCycles();
            return true;
        }
        return false;
    }
}
