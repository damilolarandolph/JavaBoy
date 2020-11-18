package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

public class Dec implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x3d:
                return dec(REGISTERS.A, cpu);
            case 0x05:
                return dec(REGISTERS.B, cpu);
            case 0x0d:
                return dec(REGISTERS.C, cpu);
            case 0x15:
                return dec(REGISTERS.D, cpu);
            case 0x1d:
                return dec(REGISTERS.E, cpu);
            case 0x25:
                return dec(REGISTERS.H, cpu);
            case 0x2d:
                return dec(REGISTERS.L, cpu);
            case 0x35:
                return decHL(cpu);
            case 0x0b:
                return dec16(RegisterPairs.BC, cpu);
            case 0x1b:
                return dec16(RegisterPairs.DE, cpu);
            case 0x2b:
                return dec16(RegisterPairs.HL, cpu);
            case 0x3b:
                return dec16SP(cpu);
            default:
                return false;
        }
    }


    private boolean dec(REGISTERS reg, CPU cpu) {
        int value = cpu.readRegister(reg);

        cpu.writeRegister(reg, applyDec(value, cpu));
        cpu.addCycles();
        return true;
    }

    private boolean decHL(CPU cpu) {
        int address = cpu.readWordRegister(RegisterPairs.HL);
        int value = cpu.readAddress(address);

        cpu.writeAddress(address, applyDec(value, cpu));
        cpu.addCycles(3);
        return true;
    }

    private boolean dec16(RegisterPairs pair, CPU cpu) {

        cpu.writeWordRegister(pair, cpu.readWordRegister(pair) - 1);
        cpu.addCycles(2);
        return true;
    }

    private boolean dec16SP(CPU cpu) {
        cpu.setSP(cpu.getSP() - 1);
        cpu.addCycles(2);
        return true;
    }

    private int applyDec(int value, CPU cpu) {
        int result = value - 0x01;
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, (value & 0xf) < 0x01);
        cpu.setFlag(FLAGS.N, true);

        return result;

    }
}
