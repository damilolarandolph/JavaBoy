package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;

import java.util.OptionalInt;

public class CCF implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0x3f) {
            cpu.writeFlag(FLAG.Cy, ~(cpu.getFlag(FLAG.Cy)));
            return OptionalInt.of(1);
        }else {
            return OptionalInt.empty();
        }
    }
}
