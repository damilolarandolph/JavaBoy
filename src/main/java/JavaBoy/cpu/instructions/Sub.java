package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

public class Sub implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x97:
                return sub(REGISTERS.A, cpu);
            case 0x90:
                return sub(REGISTERS.B, cpu);
            case 0x91:
                return sub(REGISTERS.C, cpu);
            case 0x92:
                return sub(REGISTERS.D, cpu);
            case 0x93:
                return sub(REGISTERS.E, cpu);
            case 0x94:
                return sub(REGISTERS.H, cpu);
            case 0x95:
                return sub(REGISTERS.L, cpu);
            case 0x96:
                return subHL(cpu);
            case 0xd6:
                return sub(cpu);
            case 0x9f:
                return sbc(REGISTERS.A, cpu);
            case 0x98:
                return sbc(REGISTERS.B, cpu);
            case 0x99:
                return sbc(REGISTERS.C, cpu);
            case 0x9a:
                return sbc(REGISTERS.D, cpu);
            case 0x9b:
                return sbc(REGISTERS.E, cpu);
            case 0x9c:
                return sbc(REGISTERS.H, cpu);
            case 0x9d:
                return sbc(REGISTERS.L, cpu);
            case 0x9e:
                return sbcHL(cpu);
            case 0xde:
                return sbc(cpu);

            default:
                return false;
        }
    }


    private boolean sub(REGISTERS reg2, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg2);
        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));
        cpu.addCycles();
        return true;
    }

    private boolean sub(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();
        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean subHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));
        cpu.writeRegister(REGISTERS.A, subBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }


    private boolean sbc(REGISTERS reg2, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(reg2) ;
        cpu.writeRegister(REGISTERS.A, subCBytes(val1, val2, cpu));
        cpu.addCycles();
        return true;
    }

    private boolean sbc(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();
        cpu.writeRegister(REGISTERS.A, subCBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private int subCBytes(int val1, int val2, CPU cpu) {
        int carry = cpu.getFlag(FLAGS.C);
        int result = (val1 - val2 - carry);
        cpu.setFlag(FLAGS.Z, (result & 0xff) == 0);
        cpu.setFlag(FLAGS.N, true);
        cpu.setFlag(FLAGS.H, ((val1 & 0x0f) - carry) < (val2 & 0x0f));
        cpu.setFlag(FLAGS.C, result < 0);

        return result;
    }

    private boolean sbcHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));
        cpu.writeRegister(REGISTERS.A, subCBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }


    private int subBytes(int val1, int val2, CPU cpu) {
        int result = (val1 & 0xff) - (val2 & 0xff);
        cpu.setFlag(FLAGS.Z, result == 0);
        boolean borrowCheck = (val1 & 0xf) < (val2 & 0xf);
        cpu.setFlag(FLAGS.H, borrowCheck);
        cpu.setFlag(FLAGS.C, val1 < val2);
        cpu.setFlag(FLAGS.N, true);
        return result;
    }
}
