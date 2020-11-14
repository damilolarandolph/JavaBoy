package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;

import java.util.OptionalInt;

public class Add implements Instruction {


    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x87:
                return add(REGISTER.A, cpu);
            case 0x80:
                return add(REGISTER.B, cpu);
            case 0x81:
                return add(REGISTER.C, cpu);
            case 0x82:
                return add(REGISTER.D, cpu);
            case 0x83:
                return add(REGISTER.E, cpu);
            case 0x84:
                return add(REGISTER.H, cpu);
            case 0x85:
                return add(REGISTER.L, cpu);
            case 0x86:
                return add(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0xc6:
                return add(cpu);
            // Add + Carry Flag
            case 0x8f:
                return addC(REGISTER.A, cpu);
            case 0x88:
                return addC(REGISTER.B, cpu);
            case 0x89:
                return addC(REGISTER.C, cpu);
            case 0x8a:
                return addC(REGISTER.D, cpu);
            case 0x8b:
                return addC(REGISTER.E, cpu);
            case 0x8c:
                return addC(REGISTER.H, cpu);
            case 0x8d:
                return addC(REGISTER.L, cpu);
            case 0x8e:
                return addC(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0xce:
                return addC(cpu);

            //16-bit Add Instructions
            case 0x09:
                return add16(new RegisterPair(REGISTER.B, REGISTER.C), cpu);
            case 0x19:
                return add16(new RegisterPair(REGISTER.D, REGISTER.E), cpu);
            case 0x29:
                return add16(new RegisterPair(REGISTER.H, REGISTER.L), cpu);
            case 0x39:
                return add16SP(cpu);
            case 0xe8:
                return addSP(cpu);
            default:
                return OptionalInt.empty();
        }

    }


    private OptionalInt add(REGISTER fromRegister, CPU cpu) {
        int reg1Val = cpu.readRegister(REGISTER.A);
        int reg2Val = cpu.readRegister(fromRegister);

        cpu.writeRegister(REGISTER.A, addBytes(reg1Val, reg2Val, cpu));
        return OptionalInt.of(4);

    }

    private OptionalInt add(RegisterPair pair, CPU cpu) {
        int val1 = cpu.readRegister(REGISTER.A);
        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeRegister(REGISTER.A, addBytes(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt add(CPU cpu) {
        int val1 = cpu.readRegister(REGISTER.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTER.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(8);

    }

    private OptionalInt addC(REGISTER second, CPU cpu) {

        int val1 = cpu.readRegister(REGISTER.A);
        int val2 = cpu.readRegister(second) + cpu.getFlag(FLAG.Cy);

        cpu.writeRegister(REGISTER.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(4);

    }

    private OptionalInt addC(CPU cpu) {
        int val1 = cpu.readRegister(REGISTER.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTER.A, addBytes(val1, val2, cpu));
        return OptionalInt.of(8);

    }

    private OptionalInt addC(RegisterPair pair, CPU cpu) {
        int val1 = cpu.readRegister(REGISTER.A);

        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeRegister(REGISTER.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private int addBytes(int value, int value2, CPU cpu) {

        int result = (value + value2) & 0xff;

        if (result == 0x0) {
            cpu.setFlag(FLAG.Z);
        } else {
            cpu.resetFlag(FLAG.Z);
        }

        if ((((value & 0xf) + (value2 & 0xf)) & 0x10) == 0x10) {
            cpu.setFlag(FLAG.H);
        } else {
            cpu.resetFlag(FLAG.H);
        }

        cpu.resetFlag(FLAG.N);

        if ((value + value2) > 0xff) {

            cpu.setFlag(FLAG.Cy);

        } else {
            cpu.resetFlag(FLAG.Cy);
        }


        return result;

    }


    private OptionalInt add16(RegisterPair pair, CPU cpu){
        RegisterPair hlPair = new RegisterPair(REGISTER.H, REGISTER.L);
        int val1 = cpu.readWordRegister(hlPair);
        int val2 = cpu.readWordRegister(pair);

        cpu.writeWordRegister(hlPair, applyAdd16(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt add16SP(CPU cpu){
        RegisterPair hlPair = new RegisterPair(REGISTER.H, REGISTER.L);
        int val1 = cpu.readWordRegister(hlPair);
        int val2 = cpu.readWordRegister(REGISTER.SP);

        cpu.writeWordRegister(hlPair, applyAdd16(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt addSP(CPU cpu){
        int val1 = cpu.readWordRegister(REGISTER.SP);
        int val2 = cpu.readPC();

        cpu.writeWordRegister(REGISTER.SP, applyAdd16(val1, val2, cpu));
        cpu.resetFlag(FLAG.Z);

        return OptionalInt.of(16);
    }


    private int applyAdd16(int val1, int val2, CPU cpu){
        int result = val1 + val2;
        boolean carry  = ((0xfff & val2) + (0xfff & val2) & 0x1000) == 0x1000;

        if(carry)
            cpu.setFlag(FLAG.H);
        else
            cpu.resetFlag(FLAG.H);

        if(result > 0xffff)
            cpu.setFlag(FLAG.Cy);
        else
            cpu.resetFlag(FLAG.Cy);


      cpu.resetFlag(FLAG.N);

      return result;

    }
}
