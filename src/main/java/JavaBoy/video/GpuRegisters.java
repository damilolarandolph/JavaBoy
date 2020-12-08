package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

public class GpuRegisters implements MemorySlot {

    private int scy = 0;
    private int scx = 0;
    private final int ly = 0;
    private int lyc = 0;
    private int wy = 0;
    private int wx = 0;

    @Override
    public int getByte(int address) {
        switch (address) {
            case 0xff42:
                return scy;
            case 0xff43:
                return scx;
            case 0xff44:
                return ly;
            case 0xff45:
                return lyc;
            case 0xff4a:
                return wy;
            default:
                return wx;
        }
    }

    @Override
    public void setByte(int address, int value) {
        switch (address) {
            case 0xff42:
                if (value > 0xff) {
                    scy = value - 0xff;
                } else {
                    scy = value;
                }
                break;
            case 0xff43:
                if (value > 0xff) {
                    scx = value - 0xff;
                } else {
                    scx = value;
                }
                break;
            case 0xff45:
                lyc = value;
                break;
            case 0xff4a:
                wy = value;
                break;
            case 0xff4b:
                wx = value;
                break;
        }
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        switch (address) {
            case 0xff42:
            case 0xff43:
            case 0xff44:
            case 0xff45:
            case 0xff4a:
            case 0xff4b:
                return true;
            default:
                return false;
        }
    }
}
