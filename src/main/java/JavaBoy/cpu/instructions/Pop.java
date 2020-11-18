package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.registers.RegisterPairs;

import java.util.OptionalInt;

public class Pop implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0xf1:
                return pop(RegisterPairs.AF, cpu);
            case 0xc1:
                return pop(RegisterPairs.BC, cpu);
            case 0xd1:
                return pop(RegisterPairs.DE, cpu);
            case 0xe1:
                return pop(RegisterPairs.HL, cpu);


            default:
                return OptionalInt.empty();

        }
    }


    private OptionalInt pop(RegisterPairs pair, CPU cpu) {
        int lowByte = cpu.popSP();
        int highByte = cpu.popSP();
        cpu.writeWordRegister(pair,(highByte << 8) | lowByte);
        return OptionalInt.of(12);
    }
}
