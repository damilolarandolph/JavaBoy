package JavaBoy.video;

public interface Renderer {
    void renderPixel(Palette.GreyShades shade);
    void hBlank();
    void vBlank();
    void requestRefresh();
}
