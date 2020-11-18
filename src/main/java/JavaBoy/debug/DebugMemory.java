package JavaBoy.debug;

import JavaBoy.memory.MemorySlot;

public class DebugMemory implements MemorySlot {
    private final byte[] data = new byte[(0x9fff - 0x8000) + 1];
    private  final byte[] bank2 = new byte[(0xffff - 0xc000) + 1];

    @Override
    public int getByte(int address) {
        if (address >= 0xc000)
            return Byte.toUnsignedInt( bank2[address - 0xc000]);
        else {
            return Byte.toUnsignedInt( data[address - 0x8000]);
        }
    }

    @Override
    public void setByte(int address, int value) {
        if (address >= 0xc000) {
            bank2[address - 0xc000] = (byte) value;
            if (address == 0xff02 && value == 0x81){
                System.out.print((char)this.getByte(0xff01));
            }
        }

        else {
            data[address - 0x8000] = (byte) value;
        }
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return (address >= 0x8000 && address <= 0x9fff) || (address >= 0xc000 && address <= 0xFFFF);
    }
}
