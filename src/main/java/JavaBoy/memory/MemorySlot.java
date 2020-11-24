package JavaBoy.memory;

public interface MemorySlot {

    int getByte(int address);

    void setByte(int address, int value);

    boolean hasAddressInSlot(int address);
}
