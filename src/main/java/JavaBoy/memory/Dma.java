package JavaBoy.memory;

public class Dma implements MemorySlot {

    final MemoryMap mmu;
    int ticks = 0;
    int data = 0;
    int nextSource = 0;
    int nextDestination = 0xfe00;
    int cycles = 0;
    boolean isTransferring = false;

    public Dma(MemoryMap mmu) {
        this.mmu = mmu;
    }

    public void tick() {
        if (isTransferring) {
            if (cycles == 160) {
                isTransferring = false;
            } else {

                    if ((nextSource & 0xff) <= 0x9f) {
                        mmu.setByte(nextDestination, mmu.readByte(nextSource));
                        ++nextSource;
                        ++nextDestination;
                    }

                ++cycles;
            }
        }
    }

    public boolean canAccess(int address) {
        return !isTransferring || (address >= 0xff80 && address <= 0xfffe);
    }


    @Override
    public int getByte(int address) {
        return this.data;
    }

    @Override
    public void setByte(int address, int value) {
        value &= 0xff;
        isTransferring = true;
        ticks = 0;
        nextDestination = 0xfe00;
        this.data = value;
        cycles = 0;
        this.nextSource = (value * 0x100);
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xff46;
    }
}
