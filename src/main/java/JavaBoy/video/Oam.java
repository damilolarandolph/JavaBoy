package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

import java.util.ArrayList;

import static JavaBoy.utils.BitUtils.getNthBit;

public class Oam implements MemorySlot {
    private final SpriteAttribute[] lineSpritesBuffer = {
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
            new SpriteAttribute(),
    };
    int[] data = new int[(0xfe9f - 0xfe00) + 1];
    ArrayList<SpriteAttribute> foundSprites = new ArrayList<>(10);

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

    public SpriteAttribute[] getSprites(int line, boolean is16) {
        for (int idx = 0; idx < 10; ++idx) {
            lineSpritesBuffer[idx].setDirty(true);
        }
        int spriteFound = 0;
        int step = is16 ? 2 : 1;
        for (int a = 0; a < 40; a += step) {
            if (spriteFound == 10)
                break;

            int yPos = data[a * 4];
            if (is16 && yPos <= 8)
                continue;
            if ((yPos == 0) || (yPos >= 160))
                continue;
            if ((line < (yPos - 16)) ||  line > yPos)
                continue;
            if (!is16 && line > (yPos - 8))
                continue;
            ++spriteFound;
            initSprite(lineSpritesBuffer[spriteFound - 1], a, is16);
        }
        return lineSpritesBuffer;
    }

    public SpriteAttribute[] getSpritesBuffer() {
        return this.lineSpritesBuffer;
    }

    private void initSprite(SpriteAttribute sprite, int spritePos,
                            boolean is16) {
        int spriteBegin = spritePos * 4;
        sprite.setIs16(is16);
        sprite.init(spritePos, data[spriteBegin],
                    data[spriteBegin + 1],
                    is16 ? data[spriteBegin + 2] & 0xfe : data[spriteBegin + 2],
                    data[spriteBegin + 3]
        );
        sprite.setDirty(false);

        if (is16) {
            if (sprite.getLowerHalf() == null){
                sprite.initLowerHalf();
            }
            int lowerBegin = (spritePos + 1) * 4;
            sprite.getLowerHalf().init(spritePos + 1, data[lowerBegin],
                                       data[lowerBegin + 1],
                                       data[spriteBegin + 2] | 0x01,
                                       data[lowerBegin + 3]);
        }
    }


    public class SpriteAttribute implements Comparable<SpriteAttribute> {

        private SpriteAttribute lowerHalf;
        private boolean is16 = false;
        private int yPosition;
        private int spriteNumber;
        private int xPosition;
        private int tileNumber;
        private Palettes palette;
        private boolean isAboveBG;
        private boolean isXFlipped;
        private boolean isYFlipped;
        private boolean isDirty = true;

        public boolean isDirty() {
            return isDirty;
        }

        void initLowerHalf(){
            this.lowerHalf = new SpriteAttribute();
        }

        void setDirty(boolean dirty) {
            isDirty = dirty;
        }

        public boolean isIs16() {
            return is16;
        }

        public void setIs16(boolean is16) {
            this.is16 = is16;
        }

         void init(int spriteNumber, int yPosition, int xPosition,
                         int tileNumber,
                         int attributes) {
            this.spriteNumber = spriteNumber;
            this.yPosition = yPosition;
            this.xPosition = xPosition;
            this.tileNumber = tileNumber;
            this.palette = getNthBit(4,
                                     attributes) == 0 ? Palettes.OBP0 :
                    Palettes.OBP1;
            this.isAboveBG = getNthBit(7, attributes) == 0;
            this.isXFlipped = getNthBit(5, attributes) == 1;
            this.isYFlipped = getNthBit(6, attributes) == 1;

        }

        SpriteAttribute getLowerHalf() {
            return this.lowerHalf;
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

        public int getTileForLine(int y) {

            if (!is16)
                return this.tileNumber;

            if (y % 16 >= 8) {
                return lowerHalf.getTileNumber();
            }

            return this.tileNumber;
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
}
