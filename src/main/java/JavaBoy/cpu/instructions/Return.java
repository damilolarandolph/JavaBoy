package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.JumpConditions;

public class Return implements Instruction {
    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xc9:
                return ret(cpu);
            case 0xd9:
                return reti(cpu);
            case 0xc0:
                return ret(JumpConditions.NZ, cpu);
            case 0xc8:
                return ret(JumpConditions.Z, cpu);
            case 0xd0:
                return ret(JumpConditions.NC, cpu);
            case 0xd8:
                return ret(JumpConditions.C, cpu);
            case 0xc7:
                return rst(0x0, cpu);
            case 0xcf:
                return rst(0x08, cpu);
            case 0xd7:
                return rst(0x10, cpu);
            case 0xdf:
                return rst(0x18, cpu);
            case 0xe7:
                return rst(0x20, cpu);
            case 0xef:
                return rst(0x28, cpu);
            case 0xf7:
                return rst(0x30, cpu);
            case 0xff:
                return rst(0x38, cpu);
            default:
                return false;
        }
    }

    private void applyRet(CPU cpu) {
        int lowByte = cpu.popSP();
        int highByte = cpu.popSP();

        cpu.setPC((highByte << 8) | lowByte);

    }

    private boolean ret(CPU cpu) {
        applyRet(cpu);
        cpu.addCycles(4);
        return true;
    }

    private boolean ret(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            applyRet(cpu);
            cpu.addCycles(5);
        } else {
            cpu.setPC(cpu.getPC() + 1);
            cpu.addCycles(2);
        }
        return true;
    }

    private boolean reti(CPU cpu) {
        applyRet(cpu);
        cpu.enableInterrupts();
        cpu.addCycles(4);
        return true;
    }


    private boolean rst(int lowP, CPU cpu) {
        cpu.pushSP(cpu.getPC() >>> 8);
        cpu.pushSP(cpu.getPC() & 0xff);
        cpu.setPC(lowP);
        cpu.addCycles(4);
        return true;
    }

}



