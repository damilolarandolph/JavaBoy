package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;

public class CPL implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0x2f) {
            int value = cpu.readRegister(REGISTERS.A);
            cpu.writeRegister(REGISTERS.A, ~(value));
            cpu.addCycles();
            return true;

        } else {
            return false;
        }
    }
}
