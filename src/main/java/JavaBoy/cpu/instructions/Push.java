package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.registers.RegisterPairs;
import static JavaBoy.utils.BitUtils.*;

import java.util.OptionalInt;

public class Push implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
         switch (opcode){
             case 0xf5:
                 return push(RegisterPairs.AF, cpu);
             case 0xc5:
                 return push(RegisterPairs.BC, cpu);
             case 0xd5:
                 return push(RegisterPairs.DE, cpu);
             case 0xe5:
                 return push(RegisterPairs.HL, cpu);
             default:
                 return OptionalInt.empty();
         }
    }

    private OptionalInt push(RegisterPairs pair, CPU cpu){
        int val = cpu.readWordRegister(pair);
        cpu.pushSP(getMsb(val));
        cpu.pushSP(getLsb(val));
        return OptionalInt.of(4);
    }
}
