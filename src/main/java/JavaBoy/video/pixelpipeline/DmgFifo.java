package JavaBoy.video.pixelpipeline;

import JavaBoy.video.Palette;
import JavaBoy.video.Palettes;

import java.util.Arrays;
import java.util.LinkedList;

public class DmgFifo implements PixelFIFO {
    final private Palette palette;
    LinkedList<Pixel> pixels = new LinkedList<>();
    boolean poppingEnabled = true;

    public DmgFifo(Palette pal) {
        this.palette = pal;
    }

    @Override
    public void push(Pixel[] pixels) {
        this.pixels.addAll(Arrays.asList(pixels));
    }

    @Override
    public Pixel getPixel() {
        return this.pixels.poll();
    }

    @Override
    public void clear() {
        this.pixels.clear();
    }

    @Override
    public boolean canPop() {
        return this.pixels.size() != 0 && this.poppingEnabled;
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
        return this.pixels.size() <= 8;
    }

    @Override
    public void overlay(Pixel[] pixels) {
        while ( pixels.length > this.pixels.size() ) {
            this.pixels.add(new Pixel(0, Palettes.OBP0));
        }
        for (int pixelStart = 0; pixelStart < 8; ++pixelStart) {
            Pixel existing = this.pixels.get(pixelStart);
            Pixel incoming = pixels[pixelStart];
            Palette.GreyShades existingShade = palette.getPaletteShade(
                    existing.getColour(), existing.getPalette());
            Palette.GreyShades incomingShade = palette.getPaletteShade(
                    incoming.getColour(), incoming.getPalette());

            if (incomingShade != Palette.GreyShades.WHITE &&
                    existingShade == Palette.GreyShades.WHITE)
                this.pixels.set(pixelStart, incoming);
        }
    }
}
