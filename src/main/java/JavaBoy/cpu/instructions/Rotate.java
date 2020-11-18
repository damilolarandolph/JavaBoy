package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;

public class Rotate implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x07:
                return rlca(cpu);
            case 0x17:
                return rla(cpu);
            case 0x0f:
                return rrca(cpu);
            case 0x1f:
                return rra(cpu);

            default:
                return false;
        }
    }


    private boolean rlca(CPU cpu) {

        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateLC(bits, cpu));

        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles();
        return true;
    }

    private int applyRotateLC(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        cpu.setFlag(FLAGS.C, msb == 1);
        bits = (bits << 1) & 0xff;
        bits = bits | msb;
        return bits;
    }

    private int applyRotateL(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        bits = (bits << 1) & 0xff;
        bits = bits | cpu.getFlag(FLAGS.C);
        cpu.setFlag(FLAGS.C, msb == 1);
        return bits;
    }


    private boolean rla(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateL(bits, cpu));
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles();
        return true;
    }

    private int applyRotateRC(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (lsb << 7) | bits;

        cpu.setFlag(FLAGS.C, lsb == 1);
        return bits;
    }

    private boolean rrca(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);

        cpu.writeRegister(REGISTERS.A, applyRotateRC(bits, cpu));
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles(2);
        return true;
    }

    private int applyRotateR(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (cpu.getFlag(FLAGS.C) << 7) | bits;
        cpu.setFlag(FLAGS.C, lsb == 1);
        return bits;
    }

    private boolean rra(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateR(bits, cpu));
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles();
        return true;
    }


}
