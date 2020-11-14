package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;
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

        if (cpu.isFlag(FLAGS.N)) {
            if (cpu.isFlag(FLAGS.C) || value > 0x99) {
                value += 0x60;
                cpu.setFlag(FLAGS.C);
            }

            if (cpu.isFlag(FLAGS.H) || getLsb(value) > 0x09) {
                value += 0x6;
            }

        } else {
            if (cpu.isFlag(FLAGS.C))
                value -= 0x60;
            if (cpu.isFlag(FLAGS.H))
                value -= 0x6;
        }

        cpu.writeFlag(FLAGS.Z, value == 0 ? 1 : 0);
        cpu.resetFlag(FLAGS.H);
        cpu.writeRegister(REGISTERS.A, value);
        return OptionalInt.of(1);
    }

}
