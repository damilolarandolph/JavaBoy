package JavaBoy.video.pixelpipeline;

import JavaBoy.video.Palette;
import JavaBoy.video.Palettes;

public class DmgFifo implements PixelFIFO {
    final private Palette palette;
    final private ArrayQueue<Pixel> pixels;
    boolean poppingEnabled = true;
    private  int size = 0;

    public DmgFifo(Palette pal) {
        this.palette = pal;
        this.pixels = new ArrayQueue<>(16);
        for (int idx = 0; idx < 16; ++idx) {
            pixels.add(new Pixel());
        }
    }

    @Override
    public void push(Pixel[] pixels) {
        for (int idx = 0; idx < 8; ++idx) {
            this.pixels.getAt(size + idx).setAboveBG(pixels[idx].getAboveBG());
            this.pixels.getAt( size + idx).setPalette(pixels[idx].getPalette());
            this.pixels.getAt(size + idx).setColour(pixels[idx].getColour());
        }

        size += 8;
    }

    @Override
    public Palette.GreyShades getPixel() {
        var pixel = pixels.poll();
        --size;
        return calculatePixel(pixel.getColour(), pixel.getPalette());
    }

    @Override
    public void clear() {
        size = 0;
        pixels.clear();
    }

    @Override
    public boolean canPop() {
        return size != 0 && this.poppingEnabled;
    }

    @Override
    public void disablePopping() {
        this.poppingEnabled = false;
    }

    @Override
    public void enablePopping() {
        this.poppingEnabled = true;
    }

    @Override
    public boolean canPush() {
        return size <= 8;
    }

    @Override
    public boolean peekIsAboveBG() {
        return this.pixels.peek().getAboveBG();
    }

    @Override
    public int peekColour() {
        return this.pixels.peek().getColour();
    }

    @Override
    public Palettes peekPalette() {
        return this.pixels.peek().getPalette();
    }

    private Palette.GreyShades calculatePixel(int pixel, Palettes palette) {
        return this.palette.getPaletteShade(pixel, palette);
    }

    @Override
    public void pushOverlay(Pixel[] overlay) {
        for (int idx = this.size; idx < 8; ++idx) {
             ++size;
            this.pixels.getAt(idx).setAboveBG(false);
            this.pixels.getAt(idx).setPalette(Palettes.OBP0);
            this.pixels.getAt(idx).setColour(0);
        }

        for (int idx = 0; idx < 8; ++idx) {
            Palette.GreyShades existingShade = calculatePixel(
                    pixels.getAt(idx).getColour(),
                    pixels.getAt(idx).getPalette());
            Palette.GreyShades incomingShade = palette.getPaletteShade(
                    overlay[idx].getColour(),
                    overlay[idx].getPalette());

            if ((incomingShade != Palette.GreyShades.WHITE &&
                    existingShade == Palette.GreyShades.WHITE) || existingShade == Palette.GreyShades.TRANSPARENT) {
                this.pixels.getAt(idx).setColour(overlay[idx].getColour());
                this.pixels.getAt(idx).setPalette(overlay[idx].getPalette());
                this.pixels.getAt(idx).setAboveBG(overlay[idx].getAboveBG());
            }
        }
    }

}
