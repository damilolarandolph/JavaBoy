package JavaBoy.cartridge.types;

public class ROM extends CartridgeType {


    public ROM(byte[] data) {
        super(data);
    }


    @Override
    public int getByte(int address) {
        if (address < 0x100)
            return bootRom[address];
        return Byte.toUnsignedInt(data[address]);
    }

    @Override
    public void setByte(int address, int value) {
    }


    @Override
    public boolean hasAddressInSlot(int address) {
        return (address >= 0x0 && address < 0x8000) || (address >= 0xA000 && address < 0xC000);
    }
}
