package JavaBoy.cpu.instructions;

import JavaBoy.cpu.*;
import JavaBoy.cpu.flags.FLAGS;

import java.util.OptionalInt;

public class Add implements Instruction {


    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x87:
                return add(REGISTERS.A, cpu);
            case 0x80:
                return add(REGISTERS.B, cpu);
            case 0x81:
                return add(REGISTERS.C, cpu);
            case 0x82:
                return add(REGISTERS.D, cpu);
            case 0x83:
                return add(REGISTERS.E, cpu);
            case 0x84:
                return add(REGISTERS.H, cpu);
            case 0x85:
                return add(REGISTERS.L, cpu);
            case 0x86:
                return add(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0xc6:
                return add(cpu);
            // Add + Carry Flag
            case 0x8f:
                return addC(REGISTERS.A, cpu);
            case 0x88:
                return addC(REGISTERS.B, cpu);
            case 0x89:
                return addC(REGISTERS.C, cpu);
            case 0x8a:
                return addC(REGISTERS.D, cpu);
            case 0x8b:
                return addC(REGISTERS.E, cpu);
            case 0x8c:
                return addC(REGISTERS.H, cpu);
            case 0x8d:
                return addC(REGISTERS.L, cpu);
            case 0x8e:
                return addC(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0xce:
                return addC(cpu);

            //16-bit Add Instructions
            case 0x09:
                return add16(new RegisterPair(REGISTERS.B, REGISTERS.C), cpu);
            case 0x19:
                return add16(new RegisterPair(REGISTERS.D, REGISTERS.E), cpu);
            case 0x29:
                return add16(new RegisterPair(REGISTERS.H, REGISTERS.L), cpu);
            case 0x39:
                return add16SP(cpu);
            case 0xe8:
                return addSP(cpu);
            default:
                return OptionalInt.empty();
        }

    }


    private OptionalInt add(REGISTERS fromREGISTERS, CPU cpu) {
        int reg1Val = cpu.readRegister(REGISTERS.A);
        int reg2Val = cpu.readRegister(fromREGISTERS);

        cpu.writeRegister(REGISTERS.A, addBytes(reg1Val, reg2Val, cpu));
        return OptionalInt.of(4);

    }

    private OptionalInt add(RegisterPair pair, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt add(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(8);

    }

    private OptionalInt addC(REGISTERS second, CPU cpu) {

        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readRegister(second) + cpu.getFlag(FLAGS.C);

        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(4);

    }

    private OptionalInt addC(CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);
        int val2 = cpu.readPC();

        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));
        return OptionalInt.of(8);

    }

    private OptionalInt addC(RegisterPair pair, CPU cpu) {
        int val1 = cpu.readRegister(REGISTERS.A);

        int val2 = cpu.readAddress(new Address(cpu.readWordRegister(pair)));

        cpu.writeRegister(REGISTERS.A, addBytes(val1, val2, cpu));

        return OptionalInt.of(8);
    }


    private int addBytes(int value, int value2, CPU cpu) {

        int result = (value + value2) & 0xff;

        if (result == 0x0) {
            cpu.setFlag(FLAGS.Z);
        } else {
            cpu.resetFlag(FLAGS.Z);
        }

        if ((((value & 0xf) + (value2 & 0xf)) & 0x10) == 0x10) {
            cpu.setFlag(FLAGS.H);
        } else {
            cpu.resetFlag(FLAGS.H);
        }

        cpu.resetFlag(FLAGS.N);

        if ((value + value2) > 0xff) {

            cpu.setFlag(FLAGS.C);

        } else {
            cpu.resetFlag(FLAGS.C);
        }


        return result;

    }


    private OptionalInt add16(RegisterPair pair, CPU cpu){
        RegisterPair hlPair = new RegisterPair(REGISTERS.H, REGISTERS.L);
        int val1 = cpu.readWordRegister(hlPair);
        int val2 = cpu.readWordRegister(pair);

        cpu.writeWordRegister(hlPair, applyAdd16(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt add16SP(CPU cpu){
        RegisterPair hlPair = new RegisterPair(REGISTERS.H, REGISTERS.L);
        int val1 = cpu.readWordRegister(hlPair);
        int val2 = cpu.readWordRegister(REGISTERS.SP);

        cpu.writeWordRegister(hlPair, applyAdd16(val1, val2, cpu));
        return OptionalInt.of(8);
    }

    private OptionalInt addSP(CPU cpu){
        int val1 = cpu.readWordRegister(REGISTERS.SP);
        int val2 = cpu.readPC();

        cpu.writeWordRegister(REGISTERS.SP, applyAdd16(val1, val2, cpu));
        cpu.resetFlag(FLAGS.Z);

        return OptionalInt.of(16);
    }


    private int applyAdd16(int val1, int val2, CPU cpu){
        int result = val1 + val2;
        boolean carry  = ((0xfff & val2) + (0xfff & val2) & 0x1000) == 0x1000;

        if(carry)
            cpu.setFlag(FLAGS.H);
        else
            cpu.resetFlag(FLAGS.H);

        if(result > 0xffff)
            cpu.setFlag(FLAGS.C);
        else
            cpu.resetFlag(FLAGS.C);


      cpu.resetFlag(FLAGS.N);

      return result;

    }
}
