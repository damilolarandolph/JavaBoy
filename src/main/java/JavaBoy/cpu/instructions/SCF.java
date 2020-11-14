package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;

import java.util.OptionalInt;

public class SCF implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
       if (opcode == 0x37){
           cpu.setFlag(FLAGS.C);
           return OptionalInt.of(1);
       }else{
           return OptionalInt.empty();
       }
    }
}
