package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

import java.util.ArrayList;

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

}
