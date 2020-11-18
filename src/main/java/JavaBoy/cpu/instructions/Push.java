package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.registers.RegisterPairs;

import static JavaBoy.utils.BitUtils.getLsb;
import static JavaBoy.utils.BitUtils.getMsb;

public class Push implements Instruction {

    @Override
    public boolean execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xf5:
                return push(RegisterPairs.AF, cpu);
            case 0xc5:
                return push(RegisterPairs.BC, cpu);
            case 0xd5:
                return push(RegisterPairs.DE, cpu);
            case 0xe5:
                return push(RegisterPairs.HL, cpu);
            default:
                return false;
        }
    }

    private boolean push(RegisterPairs pair, CPU cpu) {
        int val = cpu.readWordRegister(pair);
        cpu.pushSP(getMsb(val));
        cpu.pushSP(getLsb(val));
        cpu.addCycles(4);
        return true;
    }
}
