package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAG;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

import static JavaBoy.utils.BitUtils.getLsb;

public class Daa implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        if (opcode == 0x27)
            return applyDAA(cpu);
        else
            return OptionalInt.empty();
    }

    private OptionalInt applyDAA(CPU cpu) {


        int value = cpu.readRegister(REGISTERS.A);

        if (cpu.isFlag(FLAG.N)) {
            if (cpu.isFlag(FLAG.Cy) || value > 0x99) {
                value += 0x60;
                cpu.setFlag(FLAG.Cy);
            }

            if (cpu.isFlag(FLAG.H) || getLsb(value) > 0x09) {
                value += 0x6;
            }

        } else {
            if (cpu.isFlag(FLAG.Cy))
                value -= 0x60;
            if (cpu.isFlag(FLAG.H))
                value -= 0x6;
        }

        cpu.writeFlag(FLAG.Z, value == 0 ? 1 : 0);
        cpu.resetFlag(FLAG.H);
        cpu.writeRegister(REGISTERS.A, value);
        return OptionalInt.of(1);
    }

}
