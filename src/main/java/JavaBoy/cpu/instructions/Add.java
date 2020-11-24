package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

import static JavaBoy.utils.ArithmeticUtils.*;

public class Add implements Instruction {


    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x87:
                return add(REGISTERS.A, cpu);
            case 0x80:
                return add(REGISTERS.B, cpu);
            case 0x81:
                return add(REGISTERS.C, cpu);
            case 0x82:
                return add(REGISTERS.D, cpu);
            case 0x83:
                return add(REGISTERS.E, cpu);
            case 0x84:
                return add(REGISTERS.H, cpu);
            case 0x85:
                return add(REGISTERS.L, cpu);
            case 0x86:
                return addHL(cpu);
            case 0xc6:
                return add(cpu);
            // Add + Carry Flag
            case 0x8f:
                return addC(REGISTERS.A, cpu);
            case 0x88:
                return addC(REGISTERS.B, cpu);
            case 0x89:
                return addC(REGISTERS.C, cpu);
            case 0x8a:
                return addC(REGISTERS.D, cpu);
            case 0x8b:
                return addC(REGISTERS.E, cpu);
            case 0x8c:
                return addC(REGISTERS.H, cpu);
            case 0x8d:
                return addC(REGISTERS.L, cpu);
            case 0x8e:
                return addCHL(cpu);
            case 0xce:
                return addC(cpu);

            //16-bit Add Instructions
            case 0x09:
                return add16(RegisterPairs.BC, cpu);
            case 0x19:
                return add16(RegisterPairs.DE, cpu);
            case 0x29:
                return add16(RegisterPairs.HL, cpu);
            case 0x39:
                return add16SP(cpu);
            case 0xe8:
                return addSP(cpu);
            default:
                return false;
        }

    }


    private boolean add(REGISTERS fromREGISTERS, CPU cpu) {
        int reg1Val = cpu.readRegister(REGISTERS.A);
        int reg2Val = cpu.readRegister(fromREGISTERS);
        cpu.writeRegister(REGISTERS.A, addBytes(reg1Val, reg2Val, cpu));
        cpu.addCycles();
        return true;
    }

    private boolean addHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));
        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean add(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();
        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean addC(REGISTERS second, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(second) + cpu.getFlag(FLAGS.C);
        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        cpu.addCycles();
        return true;

    }

    private boolean addC(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC() + cpu.getFlag(FLAGS.C);
        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean addCHL(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(cpu.readWordRegister(RegisterPairs.HL));
        val2 = val2 + cpu.getFlag(FLAGS.C);
        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }


    private int addBytes(int value, int value2, CPU cpu) {

        int result = (value + value2) & 0xff;
        cpu.setFlag(FLAGS.Z, result == 0);
        cpu.setFlag(FLAGS.H, isHalfCarry8(value, value2));
        cpu.setFlag(FLAGS.N, false);
        cpu.setFlag(FLAGS.C, isCarry8(value, value2));
        return result;

    }


    private boolean add16(RegisterPairs pair, CPU cpu) {
        int val1 = cpu.readWordRegister(RegisterPairs.HL);
        int val2 = cpu.readWordRegister(pair);

        cpu.writeWordRegister(RegisterPairs.HL, applyAdd16(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean add16SP(CPU cpu) {
        int val1 = cpu.readWordRegister(RegisterPairs.HL);
        int val2 = cpu.getSP();

        cpu.writeWordRegister(RegisterPairs.HL, applyAdd16(val1, val2, cpu));
        cpu.addCycles(2);
        return true;
    }

    private boolean addSP(CPU cpu) {
        int val1 = cpu.getSP();
        int val2 = cpu.readPC();

        cpu.setSP(applyAdd16(val1, val2, cpu));
        cpu.setFlag(FLAGS.Z, false);
        cpu.addCycles(4);
        return true;
    }


    private int applyAdd16(int val1, int val2, CPU cpu) {
        int result = val1 + val2;
        cpu.setFlag(FLAGS.H, isHalfCarry16(val1, val2));
        cpu.setFlag(FLAGS.C, isCarry16(val1, val2));
        cpu.setFlag(FLAGS.N, false);
        return result;

    }
}
