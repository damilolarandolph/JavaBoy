package JavaBoy.video;

import static JavaBoy.utils.BitUtils.getNthBit;

public class SpriteAttribute implements Comparable<SpriteAttribute> {

    final private int yPosition;
    final private int spriteNumber;
    final private int xPosition;
    final private int tileNumber;
    final private Palettes palette;
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
                                 byte3) == 0 ? Palettes.OBP0 :
                Palettes.OBP1;
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

    public Palettes getPalette() {
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
