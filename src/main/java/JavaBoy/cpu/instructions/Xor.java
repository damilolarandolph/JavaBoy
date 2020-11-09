package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Xor implements Instruction {

    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode){
            case 0xaf:
                return xor(REGISTER.A, cpu);
            case 0xa8:
                return xor(REGISTER.B, cpu);
            case 0xa9:
                return xor(REGISTER.C, cpu);
            case 0xaa:
                return xor(REGISTER.D, cpu);
            case 0xab:
                return  xor(REGISTER.E, cpu);
            case 0xac:
                return xor(REGISTER.H, cpu);
            case 0xad:
                return xor(REGISTER.L, cpu);
            case 0xae:
                return xor(new RegisterPair(REGISTER.H,REGISTER.L), cpu);
            case 0xee:
                return xor(cpu);
            default:
                return OptionalInt.empty();
        }
    }

    private OptionalInt xor(REGISTER reg, CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readByteRegister(reg);

        cpu.writeByteRegister(REGISTER.A, applyXOR(val1, val2, cpu));
        return  OptionalInt.of(4);
    }

    private OptionalInt xor(RegisterPair pair, CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readFromAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeByteRegister(REGISTER.A, applyXOR(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private OptionalInt xor(CPU cpu){
        int val1 = cpu.readByteRegister(REGISTER.A);
        int val2 = cpu.readByteFromPC();
        cpu.writeByteRegister(REGISTER.A, applyXOR(val1, val2, cpu));

        return OptionalInt.of(8);
    }

    private int applyXOR(int val1, int val2, CPU cpu) {
        int result = (val1 & 0xff) ^ (val2 & 0xff);

        if (result == 0x0) {
            cpu.setFlag(FLAG.Z);

        } else {
            cpu.resetFlag(FLAG.Z);
        }

        cpu.resetFlag(FLAG.Cy);
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);


        return result;

    }
}

