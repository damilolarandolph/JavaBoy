package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.instructions.jumpconditions.JumpConditions;
import JavaBoy.cpu.registers.RegisterPairs;

public class Jump implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xc3:
                return jp(cpu);
            case 0xc2:
                return jp(JumpConditions.NZ, cpu);
            case 0xca:
                return jp(JumpConditions.Z, cpu);
            case 0xd2:
                return jp(JumpConditions.NC, cpu);
            case 0xda:
                return jp(JumpConditions.C, cpu);
            case 0x18:
                return jr(cpu);
            case 0x20:
                return jr(JumpConditions.NZ, cpu);
            case 0x28:
                return jr(JumpConditions.Z, cpu);
            case 0x30:
                return jr(JumpConditions.NC, cpu);
            case 0x38:
                return jr(JumpConditions.C, cpu);
            case 0xe9:
                return jpHL(cpu);
            default:
                return false;

        }
    }

    private boolean jp(CPU cpu) {
        int word = cpu.readWordPC();
        cpu.setPC(word);
        cpu.addCycles(4);
        return true;
    }

    private boolean jp(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jp(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 2);
            cpu.addCycles(3);
            return true;
        }
    }

    private boolean jpHL(CPU cpu) {
        int value = cpu.readWordRegister(RegisterPairs.HL);
        cpu.setPC(value);
        cpu.addCycles();
        return true;
    }

    private boolean jr(CPU cpu) {
        int value = cpu.readPC();
        applyJR(value, cpu);
        cpu.addCycles(3);
        return true;
    }

    private boolean jr(JumpConditions condition, CPU cpu) {
        if (condition.test(cpu)) {
            return jr(cpu);
        } else {
            cpu.setPC(cpu.getPC() + 1);
            cpu.addCycles(2);
            return true;
        }
    }

    private void applyJR(int value, CPU cpu) {
        byte signedByte = (byte) value;
        cpu.setPC(cpu.getPC() + signedByte);

    }
}

