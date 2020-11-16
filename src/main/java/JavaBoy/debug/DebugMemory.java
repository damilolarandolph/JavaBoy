package JavaBoy.debug;

import JavaBoy.memory.MemorySlot;

public class DebugMemory implements MemorySlot {
    private final byte[] data = new byte[(0xffff - 0x8000) - 0x1fff];

    @Override
    public int getByte(int address) {
        if (address >= 0xc000)
            return data[address - 0x1ffff];
        return data[address];
    }

    @Override
    public void setByte(int address, int value) {
        if (address >= 0xc000)
            data[address - 0x1ffff] = (byte) value;
        data[address] = (byte) value;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return (address >= 0x8000 && address <= 0x9fff) || (address >= 0xc000 && address <= 0xFFFF);
    }
}
