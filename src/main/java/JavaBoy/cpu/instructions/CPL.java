package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class CPL implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0x2f){
            int value = cpu.readByteRegister(REGISTER.A);
            cpu.writeByteRegister(REGISTER.A, ~(value));
            return OptionalInt.of(1);

        }else {
            return OptionalInt.empty();
        }
    }
}
