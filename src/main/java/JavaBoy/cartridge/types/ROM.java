package JavaBoy.cartridge.types;

class ROM extends CartridgeType {


    public ROM(byte[] data) {
        super(data);
    }


    @Override
    public int getByte(int address) {
        return data[address];
    }

    @Override
    public void setByte(int address, int value) {
    }


    @Override
    public boolean hasAddressInSlot(int address) {
        return (address >= 0x0 && address < 0x8000) || (address >= 0xA000 && address < 0xC000);
    }
}
