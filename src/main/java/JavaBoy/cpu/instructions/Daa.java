package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;

import static JavaBoy.utils.BitUtils.getLsb;

public class Daa implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        if (opcode == 0x27) {

            applyDAA(cpu);
            cpu.addCycles();
            return true;
        } else {
            return false;
        }
    }

    private void applyDAA(CPU cpu) {
        int adjustment = 0;
        int aReg = cpu.readRegister(REGISTERS.A);
        if (cpu.isFlag(FLAGS.C) || (aReg > 0x99 && !cpu.isFlag(FLAGS.N))){
            adjustment = 0x60;
            cpu.setFlag(FLAGS.C, true);
        }

        if (cpu.isFlag(FLAGS.H) || (aReg & 0x0f) > 0x09 && !cpu.isFlag(FLAGS.N)){
            adjustment += 0x06;
        }

        aReg += cpu.isFlag(FLAGS.N) ? -(adjustment) : adjustment;
        aReg &= 0xff;

        cpu.setFlag(FLAGS.Z, aReg == 0);
        cpu.setFlag(FLAGS.H, false);
        cpu.writeRegister(REGISTERS.A, aReg);

    }

}
