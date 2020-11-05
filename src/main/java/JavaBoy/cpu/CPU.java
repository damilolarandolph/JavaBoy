package JavaBoy.cpu;

import JavaBoy.cartridge.Cartridge;
import JavaBoy.memory.MemoryMap;

public interface CPU {

    Registers getRegisters();
    MemoryMap getMemoryMap();
    void incrementPC();
    int getPC();
}
