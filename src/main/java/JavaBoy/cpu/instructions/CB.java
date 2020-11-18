package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;

public class CB implements Instruction {
    final Instruction[] instructions;

    public CB(Instruction[] instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0xcb) {
            int cbOpcode = cpu.readPC();
            cpu.addCycles();
            boolean result;
            for (Instruction instruction : instructions) {
                result = instruction.execute(cbOpcode, cpu);
                if (result)
                    return true;
            }
        }
        return false;
    }
}
