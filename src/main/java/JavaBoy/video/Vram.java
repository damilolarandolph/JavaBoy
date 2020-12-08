package JavaBoy.video;

import JavaBoy.memory.MemorySlot;
import static JavaBoy.utils.BitUtils.*;

public class Vram implements MemorySlot {

    int[] data = new int[0x9800 - 0x8000];
    int[] bgMap1 = new int[0x9bff - 0x9801];
    int[] bgMap2 = new int[0x9fff - 0x9c01];


    /**
     * @param tileNum        The tile number in the tile data table
     * @param x              The pixel's column
     * @param y              The pixel's row
     * @param addressingMode The addressing being used to access the tile
     *                       data table
     * @return 6 bit value representing a pixel
     */
    public int getTilePixel(int tileNum, int x, int y,
                            LCDC.AddressingModes addressingMode) {
        int result = 0;
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
        int lineStart = tileStart + (y * 2);
        int byte1  = data[(block + lineStart) - 0x8000];
        int byte2 = data[(block + (lineStart + 1)) - 0x8000];

        // Making the right most column 0 and the left most 7
        x = 7 - x;
        return  (getNthBit(x, byte2) << 1) & (getNthBit(x, byte1));

    }

    /**
     * @param x              The BG pixel's column
     * @param y              The BG pixel's row
     * @param isMap1         Sets which BG map to use (1 or 2)
     * @param addressingMode The addressing mode being used to access the tile
     *                       data table
     * @return A 6 bit value representing a pixel
     */
    public int getPixelBG(int x,
                          int y,
                          boolean isMap1,
                          LCDC.AddressingModes addressingMode) {

        // If the x or y coordinates exceed the bounds of the map,
        // wrap to the opposite side on the respective coordinate.
        if (x > 255) x -= 256;
        if (y > 255) y -= 256;

        // Determining which slot in the map the pixel lies
        int mapX = (255 - (255 - x)) / 8;
        int mapY = (255 - (255 - y)) / 8;

        int tile;
        if (isMap1) {
            tile = bgMap1[(mapY * 32) + mapX];
        } else {
            tile = bgMap2[(mapY * 32) + mapX];
        }

        // Getting the x and y coordinates of the pixel on the
        // tile
        int tileX = x % 8;
        int tileY = y % 8;

        return getTilePixel(tile, tileX, tileY, addressingMode);
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
        return (address >= 0x8000) && (address <= 0x9fff);
    }
}
