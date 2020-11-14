package JavaBoy.cpu.instructions;

import JavaBoy.cpu.Address;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.FLAG;
import JavaBoy.cpu.REGISTER;

import java.util.OptionalInt;

public class Swap implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0x37:
                return swap(REGISTER.A, cpu);
            case 0x30:
                return swap(REGISTER.B, cpu);
            case 0x31:
                return swap(REGISTER.C, cpu);
            case 0x32:
                return swap(REGISTER.D, cpu);
            case 0x33:
                return swap(REGISTER.E, cpu);
            case 0x34:
                return swap(REGISTER.H, cpu);
            case 0x35:
                return swap(REGISTER.L, cpu);
            case 0x36:
                return swap(cpu);
            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt swap(REGISTER reg, CPU cpu) {
        int bits = cpu.readRegister(reg);
        int result = applySwap(bits, cpu);
        cpu.writeRegister(reg, result);
        return OptionalInt.of(4);
    }

    private OptionalInt swap(CPU cpu){
        Address addr = new Address(cpu.readWordRegister(REGISTER.H, REGISTER.L));
        int bits = cpu.readAddress(addr);
        int result = applySwap(bits, cpu);
        cpu.writeAddress(addr, result);
        return OptionalInt.of(16);
    }

    private int applySwap(int val, CPU cpu) {
        int lsb = 0xf & val;
        int msb = val >>> 4;
        int result = (lsb << 4) | msb;

        cpu.resetFlag(FLAG.Cy);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.writeFlag(FLAG.Z, result == 0 ? 1 : 0);

        return result;
    }
}


