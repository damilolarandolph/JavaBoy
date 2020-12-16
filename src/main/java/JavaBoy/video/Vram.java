package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

import static JavaBoy.utils.BitUtils.getNthBit;

public class Vram implements MemorySlot {

    int[] data = new int[(0x97ff - 0x8000) + 1];
    int[] bgMap1 = new int[(0x9bff - 0x9800) + 1];
    int[] bgMap2 = new int[(0x9fff - 0x9c00) + 1];


    /**
     * @param tileNum        The tile number in the tile data table
     * @param addressingMode The addressing being used to access the tile
     *                       data table
     * @return A {@link Tile} instance
     */
    public Tile getTile(int tileNum,
                        LCDC.AddressingModes addressingMode) {
        int block;
        if (addressingMode == LCDC.AddressingModes.M8000) {
            block = 0x8000;
        } else {
            if (tileNum > 127) {
                tileNum -= 128;
                block = 0x8800;
            } else {
                block = 0x9000;
            }
        }
        int tileStart = tileNum * 16;
        return new Tile(tileStart, block);
    }

    /**
     * @param x              The BG pixel's column
     * @param y              The BG pixel's row
     * @param map            Dictates which background map to use
     * @param addressingMode The addressing mode being used to access the tile
     *                       data table
     * @return A {@link Tile} instance
     */
    public Tile getTileBG(int x,
                          int y,
                          BGMaps map,
                          LCDC.AddressingModes addressingMode) {

        // If the x or y coordinates exceed the bounds of the map,
        // wrap to the opposite side on the respective coordinate.
        if (x > 255) x -= 256;
        if (y > 255) y -= 256;

        // Determining which slot in the map the pixel lies
        int mapX = (255 - (255 - x)) / 8;
        int mapY = (255 - (255 - y)) / 8;

        int tile = 0;
        switch (map) {
            case MAP1:
                tile = bgMap1[(mapY * 32) + mapX];
                break;
            case MAP2:
                tile = bgMap2[(mapY * 32) + mapX];
                break;
        }

        return getTile(tile, addressingMode);
    }

    @Override
    public int getByte(int address) {
        if (address <= 0x97ff)
            return data[address - 0x8000];
        else if (address <= 0x9bff)
            return bgMap1[address - 0x9800];
        else
            return bgMap2[address - 0x9c00];
    }

    @Override
    public void setByte(int address, int value) {
        value &= 0xff;
        if (address <= 0x97ff)
            data[address - 0x8000] = value;
        else if (address <= 0x9bff)
            bgMap1[address - 0x9800] = value;
        else
            bgMap2[address - 0x9c00] = value;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address >= 0x8000 && address <= 0x9fff;
    }

    public enum BGMaps {
        MAP1, MAP2
    }

    public class Tile {
        private final int tileStart;
        private final int block;


        public Tile(int tileStart, int block) {
            this.tileStart = tileStart;
            this.block = block;
        }

        /**
         * @param line The line of the Tile to be fetched valid
         *             values are from 0 to 7
         * @return An array of colour numbers;
         */
        public int[] getLine(int line) {
            int[] pixels = new int[8];
            int lineStart = tileStart + (line * 2);
            int byte1 = data[(block + lineStart) - 0x8000];
            int byte2 = data[(block + (lineStart + 1)) - 0x8000];
            for (int a = 7; a >= 0; --a) {
                int palette = (getNthBit(a, byte2) << 1) | getNthBit(a, byte1);
                pixels[7 - a] = palette;
            }
            return pixels;

        }
    }
}
