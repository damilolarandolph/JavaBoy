package JavaBoy.video.pixelpipeline;

import JavaBoy.video.Palettes;

public class Pixel {
    private int colour;
    private Palettes palette;
    private boolean isAboveBG;

    public void setAboveBG(boolean aboveBG) {
        isAboveBG = aboveBG;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setPalette(Palettes palette) {
        this.palette = palette;
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
