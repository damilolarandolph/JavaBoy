package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

public class Palette implements MemorySlot {

    int bgb = 0;
    int objPal1 = 0;
    int objPal2 = 0;

    private GreyShades getShade(int colourIndex) {
        colourIndex &= 0x03;
        switch (colourIndex){
            case 1:
                return GreyShades.LIGHT_GREY;
            case 2:
                return GreyShades.DARK_GREY;
            case 3:
                return GreyShades.BLACK;

        }
        return GreyShades.WHITE;
    }

    public GreyShades getPaletteShade(int colour, Palettes palette) {
        switch (palette) {
            case OBP0:
                return getObjPal1(colour);
            case OBP1:
                return getObjPal2(colour);
            default:
                return getBGP(colour);
        }
    }

    private GreyShades getBGP(int colourNum) {

        switch (colourNum) {
            case 3:
                return getShade(bgb >>> 6);
            case 2:
                return getShade(bgb >>> 4);
            case 1:
                return getShade(bgb >>> 2);
            case 0:
                return getShade(bgb);
        }

        return null;

    }

    private GreyShades getObjPal1(int colourNum) {

        switch (colourNum) {
            case 3:
                return getShade(objPal1 >>> 6);
            case 2:
                return getShade(objPal1 >>> 4);
            case 1:
                return getShade(objPal1 >>> 2);
        }
        return GreyShades.TRANSPARENT;
    }

    private GreyShades getObjPal2(int colourNum) {

        switch (colourNum) {
            case 3:
                return getShade(objPal2 >>> 6);
            case 2:
                return getShade(objPal2 >>> 4);
            case 1:
                return getShade(objPal2 >>> 2);
        }
        return GreyShades.TRANSPARENT;
    }

    @Override
    public int getByte(int address) {
        if (address == 0xff47)
            return bgb;
        if (address == 0xff48)
            return objPal1;

        return objPal2;
    }

    @Override
    public void setByte(int address, int value) {
        if (address == 0xff47)
            bgb = value;
        else if (address == 0xff48)
            objPal1 = value;
        else if (address == 0xff49)
            objPal2 = value;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        switch (address) {
            case 0xff47:
            case 0xff48:
            case 0xff49:
                return true;
            default:
                return false;
        }
    }

    public enum GreyShades {
        WHITE, LIGHT_GREY, DARK_GREY, BLACK, TRANSPARENT
    }
}
