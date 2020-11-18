/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package JavaBoy;

import JavaBoy.cartridge.Cartridge;
import JavaBoy.cpu.CPU;
import JavaBoy.cpu.flags.FlagBank;
import JavaBoy.cpu.instructions.*;
import JavaBoy.cpu.registers.Register16;
import JavaBoy.cpu.registers.RegisterBank;
import JavaBoy.debug.DebugMemory;
import JavaBoy.memory.MemoryMap;
import JavaBoy.memory.MemorySlot;

import java.io.File;

public class App {
    public static void main(String[] args) {
        File file = new File(    App.class.getResource("/gb-test-roms/cpu_instrs/individual/06_ld_r_r.gb").getFile());
        Cartridge cart = new Cartridge(file);

        Instruction[] instructions = new Instruction[]{
                new Add(),
                new And(),
                new CB(new Instruction[]{
                        new Bit(),
                        new Swap(),
                        new Reset(),
                        new Set(),
                        new RotateCB(),
                        new Shift()
                }),
                new Call(),
                new CCF(),
                new Cp(),
                new CPL(),
                new Daa(),
                new Dec(),
                new Inc(),
                new Jump(),
                new Load(),
                new Nop(),
                new Or(),
                new Pop(),
                new Push(),
                new Return(),
                new Rotate(),
                new SCF(),
                new Sub(),
                new Xor()
        };

        MemoryMap map = new MemoryMap(new MemorySlot[]{cart, new DebugMemory()});
        RegisterBank registers = new RegisterBank(new FlagBank(), new Register16(), new Register16());
        CPU cpu = new CPU(map, instructions, registers);
        cpu.run();
    }

    public String getGreeting() {
        return "Hello world.";
    }
}
