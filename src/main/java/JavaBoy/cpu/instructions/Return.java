package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.JumpConditions;

import java.util.OptionalInt;

public class Return implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
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
                return OptionalInt.empty();
        }
    }

    private void applyRet(CPU cpu) {
        int lowByte = cpu.popSP();
        int highByte = cpu.popSP();

        cpu.setPC((highByte << 8) | lowByte);

    }

    private OptionalInt ret(CPU cpu) {
        applyRet(cpu);
        return OptionalInt.of(16);
    }

    private OptionalInt ret(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            applyRet(cpu);
            return OptionalInt.of(20);
        } else {
            cpu.setPC(cpu.getPC() + 1);
            return OptionalInt.of(8);
        }
    }

    private OptionalInt reti(CPU cpu) {
        applyRet(cpu);
        cpu.enableInterrupts();
        return OptionalInt.of(16);
    }


    private OptionalInt rst(int lowP, CPU cpu) {
        cpu.pushSP(cpu.getPC() >>> 8);
        cpu.pushSP(cpu.getPC() & 0xff);

        cpu.setPC(lowP);


        return OptionalInt.of(16);
    }

}



