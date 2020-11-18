package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import static JavaBoy.utils.BitUtils.getLsn;
import static JavaBoy.utils.BitUtils.getMsn;

public class Swap implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x37:
                return swap(REGISTERS.A, cpu);
            case 0x30:
                return swap(REGISTERS.B, cpu);
            case 0x31:
                return swap(REGISTERS.C, cpu);
            case 0x32:
                return swap(REGISTERS.D, cpu);
            case 0x33:
                return swap(REGISTERS.E, cpu);
            case 0x34:
                return swap(REGISTERS.H, cpu);
            case 0x35:
                return swap(REGISTERS.L, cpu);
            case 0x36:
                return swap(cpu);
            default:
                return false;
        }
    }


    private boolean swap(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applySwap(bits, cpu);
        cpu.writeRegister(reg, result);
        cpu.addCycles();
        return true;
    }

    private boolean swap(CPU cpu) {
        int addr = cpu.readWordRegister(RegisterPairs.HL);
        int bits = cpu.readAddress(addr);
        int result = applySwap(bits, cpu);
        cpu.writeAddress(addr, result);
        cpu.addCycles(4);
        return true;
    }

    private int applySwap(int val, CPU cpu) {
        int result = (getLsn(val) << 4) | getMsn(val);
        cpu.setFlag(FLAGS.C, false);
        cpu.setFlag(FLAGS.H, false);
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.Z, result == 0);
        return result;
    }
}


