package JavaBoy.video.pixelpipeline;

import JavaBoy.video.Palettes;

public class Pixel {
    final private int colour;
    final private Palettes palette;
    final private boolean isAboveBG;

    public Pixel(int colour, Palettes palette) {
        this.colour = colour;
        this.palette = palette;
        this.isAboveBG = false;
    }

    public Pixel(int colour, Palettes palette, boolean BGPri) {
        this.colour = colour;
        this.palette = palette;
        this.isAboveBG = BGPri;
    }

    public boolean getAboveBG() {
        return isAboveBG;
    }

    public Palettes getPalette() {
        return palette;
    }

    public int getColour() {
        return colour;
    }
}
