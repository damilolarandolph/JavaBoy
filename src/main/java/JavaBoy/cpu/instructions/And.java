package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

public class And implements Instruction {


    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xa7:
                return and(REGISTERS.A, cpu);
            case 0xa0:
                return and(REGISTERS.B, cpu);
            case 0xa1:
                return and(REGISTERS.C, cpu);
            case 0xa2:
                return and(REGISTERS.D, cpu);
            case 0xa3:
                return and(REGISTERS.E, cpu);
            case 0xa4:
                return and(REGISTERS.H, cpu);
            case 0xa5:
                return and(REGISTERS.L, cpu);
            case 0xa6:
                return andHL(cpu);
            case 0xe6:
                return and(cpu);
            default:
                return false;
        }
    }


    private boolean and(REGISTERS reg, CPU cpu) {

        int reg1 = cpu.readRegister(REGISTERS.A);
        int reg2 = cpu.readRegister(reg);

        cpu.writeRegister(REGISTERS.A, applyAnd(reg1, reg2, cpu));
        cpu.addCycles();
        return true;
    }

    private boolean and(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTERS.A, applyAnd(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    boolean andHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));

        cpu.writeRegister(REGISTERS.A, applyAnd(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }


    int applyAnd(int val1, int val2, CPU cpu) {
        int result = (val1 & 0xff) & (val2 & 0xff);
        cpu.setFlag(FLAGS.Z, result == 0);

        cpu.setFlag(FLAGS.H, true);
        cpu.setFlag(FLAGS.C, false);
        cpu.setFlag(FLAGS.N, false);
        return result;
    }


}
