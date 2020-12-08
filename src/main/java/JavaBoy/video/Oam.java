package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

import java.util.ArrayList;

import static JavaBoy.utils.BitUtils.getNthBit;

public class Oam implements MemorySlot {
    int[] data = new int[0xfe9f - 0xfe01];

    @Override
    public int getByte(int address) {
        return data[address - 0xfe00];
    }

    @Override
    public void setByte(int address, int value) {
        this.data[address - 0xfe00] = value;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address >= 0xfe00 && address <= 0xfe9f;
    }

    public ArrayList<SpriteAttribute> getSprites(int line) {
        ArrayList<SpriteAttribute> foundSprites = new ArrayList<>();

        for (int a = 0; a < 40; ++a){
           int yPos = data[a * 4];
            if (yPos != 0 && yPos < 160){
                foundSprites.add(getSprite(a, foundSprites.size() + 1));
            }
        }
        return foundSprites;

    }

    private SpriteAttribute getSprite(int spritePos, int spriteNumber) {
        int spriteBegin = spritePos * 4;

        return new SpriteAttribute(spriteNumber, data[spriteBegin],
                                   data[spriteBegin + 1], data[spriteBegin + 2],
                                   data[spriteBegin + 3]);
    }

    public static class SpriteAttribute implements Comparable<SpriteAttribute> {

        final private int yPosition;
        final private int spriteNumber;
        final private int xPosition;
        final private int tileNumber;
        final private Palette.Palettes palette;
        final private boolean isAboveBG;
        final private boolean isXFlipped;
        final private boolean isYFlipped;

        SpriteAttribute(int spriteNumber, int byte0, int byte1, int byte2,
                        int byte3) {
            this.spriteNumber = spriteNumber;
            this.yPosition = byte0;
            this.xPosition = byte1;
            this.tileNumber = byte2;
            this.palette = getNthBit(7,
                                     byte3) == 0 ? Palette.Palettes.OBP0 :
                    Palette.Palettes.OBP1;
            this.isAboveBG = getNthBit(7, byte3) == 0;
            this.isXFlipped = getNthBit(5, byte3) == 1;
            this.isYFlipped = getNthBit(6, byte3) == 1;

        }


        @Override
        public int compareTo(SpriteAttribute spriteAttribute) {
            int result = 0;
            if (this.getXPosition() < spriteAttribute.getXPosition()) {
                ++result;
            } else if (this.getXPosition() > spriteAttribute.getXPosition()) {
                --result;
            } else {
                if (this.getSpriteNumber() < spriteAttribute.getSpriteNumber())
                    ++result;
                else
                    --result;
            }
            return result;
        }

        public int getTileNumber() {
            return tileNumber;
        }

        public int getYPosition() {
            return yPosition;
        }

        public int getXPosition() {
            return xPosition;
        }

        public Palette.Palettes getPalette() {
            return palette;
        }

        public boolean isAboveBG() {
            return isAboveBG;
        }

        public boolean isXFlipped() {
            return isXFlipped;
        }

        public boolean isYFlipped() {
            return isYFlipped;
        }

        public int getSpriteNumber() {
            return spriteNumber;
        }
    }
}
