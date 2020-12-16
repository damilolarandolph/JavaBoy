package JavaBoy.video.pixelpipeline;

public interface PixelFIFO {

    void push(Pixel[] pixels);
    Pixel getPixel();
    void clear();
    boolean canPop();
    void disablePopping();
    void enablePopping();
    boolean canPush();
    void overlay(Pixel[] pixels);

}
