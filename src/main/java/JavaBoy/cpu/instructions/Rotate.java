package JavaBoy.cpu.instructions;

import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FLAG;
import JavaBoy.cpu.REGISTERS;

import java.util.OptionalInt;

public class Rotate implements Instruction {
    @Override
    public OptionalInt execute(int opcode, CPU cpu) {
        switch (opcode) {
            case 0x07:
                return rlca(cpu);
            case 0x17:
                return rla(cpu);
            case 0x0f:
                return rrca(cpu);
            case 0x1f:
                return rra(cpu);
            //handling CB prefixed rotate instructions
            case 0xcb:
                int cbOpcode = cpu.readPC();


            default:
                return OptionalInt.empty();
        }
    }


    private OptionalInt rlca(CPU cpu) {

        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateLC(bits, cpu));

        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.resetFlag(FLAG.Z);
        return OptionalInt.of(4);
    }

    private int applyRotateLC(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        cpu.writeFlag(FLAG.Cy, msb);
        bits = (bits << 1) & 0xff;
        bits = bits | msb;
        return bits;
    }

    private int applyRotateL(int val, CPU cpu) {
        int bits = val;
        int msb = bits >>> 7;
        bits = (bits << 1) & 0xff;
        bits = bits | cpu.getFlag(FLAG.Cy);
        cpu.writeFlag(FLAG.Cy, msb);
        return bits;
    }


    private OptionalInt rla(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateL(bits, cpu));
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.resetFlag(FLAG.Z);

        return OptionalInt.of(4);
    }

    private int applyRotateRC(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (lsb << 7) | bits;

        cpu.writeFlag(FLAG.Cy, lsb);
        return bits;
    }

    private OptionalInt rrca(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);

        cpu.writeRegister(REGISTERS.A, applyRotateRC(bits, cpu));
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.resetFlag(FLAG.Z);

        return OptionalInt.of(4);
    }

    private int applyRotateR(int val, CPU cpu) {
        int bits = val;
        int lsb = bits & 0x1;
        bits = (bits >>> 1) & 0xff;
        bits = (cpu.getFlag(FLAG.Cy) << 7) | bits;
        cpu.writeFlag(FLAG.Cy, lsb);
        return bits;
    }

    private OptionalInt rra(CPU cpu) {
        int bits = cpu.readRegister(REGISTERS.A);
        cpu.writeRegister(REGISTERS.A, applyRotateR(bits, cpu));
        cpu.resetFlag(FLAG.H);
        cpu.resetFlag(FLAG.N);
        cpu.resetFlag(FLAG.Z);

        return OptionalInt.of(4);
    }


}
