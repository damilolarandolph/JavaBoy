package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class Swap implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
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
                return OptionalInt.empty();
        }
    }


    private OptionalInt swap(REGISTERS reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applySwap(bits, cpu);
        cpu.writeRegister(reg, result);
        return OptionalInt.of(4);
    }

    private OptionalInt swap(CPU cpu){
        Address addr = new Address(cpu.readWordRegister(REGISTERS.H, REGISTERS.L));
        int bits = cpu.readAddress(addr);
        int result = applySwap(bits, cpu);
        cpu.writeAddress(addr, result);
        return OptionalInt.of(16);
    }

    private int applySwap(int val, CPU cpu) {
        int lsb = 0xf & val;
        int msb = val >>> 4;
        int result = (lsb << 4) | msb;

        cpu.resetFlag(FLAGS.Cy);
        cpu.resetFlag(FLAGS.H);
        cpu.resetFlag(FLAGS.N);
        cpu.writeFlag(FLAGS.Z, result == 0 ? 1 : 0);

        return result;
    }
}


