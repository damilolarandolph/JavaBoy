package JavaBoy.video;

import JavaBoy.memory.MemorySlot;
import JavaBoy.utils.BitUtils;

public class LCDC implements MemorySlot {
    int data = 0;


    public boolean isLCD() {
        return BitUtils.getNthBit(7, data) == 1;
    }

    public int getWindowSelect() {
        int select = BitUtils.getNthBit(6, data);

        return select == 0 ? 0x9800 : 0x9c00;
    }

    public boolean isWindowDisplay() {
        return BitUtils.getNthBit(5, data) == 1;
    }

    public int getBGWindowSelect() {
        int select = BitUtils.getNthBit(4, data);

        return select == 0 ? 0x8800 : 0x8000;
    }

    public int getBGTileSelect() {
        int select = BitUtils.getNthBit(3, data);
        return select == 0 ? 0x9800 : 0x9c00;
    }

    public boolean isOBJSize() {
        return BitUtils.getNthBit(2, data) == 1;
    }

    public boolean isOBJEnable() {
        return BitUtils.getNthBit(1, data) == 1;
    }

    public boolean isPriority() {
        return BitUtils.getNthBit(0, data) == 1;
    }

    @Override
    public int getByte(int address) {
        return data;
    }

    @Override
    public void setByte(int address, int value) {
        data = value & 0xff;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xff40;
    }

    public enum AddressingModes {
        M8000, M8800, M9800, M9C00
    }
}
