package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class CPL implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0x2f){
            int value = cpu.readRegister(REGISTERS.A);
            cpu.writeRegister(REGISTERS.A, ~(value));
            return OptionalInt.of(4);

        }else {
            return OptionalInt.empty();
        }
    }
}
