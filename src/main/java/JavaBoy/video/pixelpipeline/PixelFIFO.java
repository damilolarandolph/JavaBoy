package JavaBoy.video.pixelpipeline;

import JavaBoy.video.Palette;
import JavaBoy.video.Palettes;

public interface PixelFIFO {

    void push(Pixel[] pixels);

    Palette.GreyShades getPixel();
    void clear();
    boolean canPop();
    void disablePopping();
    void enablePopping();
    boolean canPush();
    boolean peekIsAboveBG();
    int peekColour();
    Palettes peekPalette();
    void pushOverlay(Pixel[] pixels);

}
