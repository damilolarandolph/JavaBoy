package JavaBoy.cpu.instructions;

import JavaBoy.cartridge.Cartridge;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.Registers;

import java.util.OptionalInt;

public interface Instruction {
    OptionalInt execute(int opcode, CPU cpu);
}
