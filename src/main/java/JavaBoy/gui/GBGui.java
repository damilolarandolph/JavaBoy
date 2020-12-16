package JavaBoy.gui;

import JavaBoy.video.Palette;
import JavaBoy.video.Renderer;

import java.awt.*;


public class GBGui implements Renderer {
    final int pixelSize = 2;
    private final int screenX = 160;
    private final int screenY = 144;
    private final Palette.GreyShades[][] pixels;
    private int x = 0;
    private  int y = 0;
    private Frame frame;
    private Canvas screen;

    public GBGui() {
        pixels = new Palette.GreyShades[screenY][screenX];
    }

    @Override
    public void renderPixel(Palette.GreyShades shade) {
        pixels[y][x] = shade;
        screen.repaint();
        ++x;
    }

    @Override
    public void hBlank() {
        x = 0;
        ++y;
    }

    @Override
    public void vBlank() {
       x = 0;
       y = 0;
    }

    public void show() {

        frame = new Frame();
        frame.setSize(600, 600);
        frame.setTitle("JavaBoy !");
        screen = new GBScreen();
        screen.setSize(pixelSize * screenX, pixelSize * screenY);
        frame.setVisible(true);
        screen.setVisible(true);
        frame.add(screen);

    }


    public class GBScreen extends Canvas {

        @Override
        public void paint(Graphics g) {
            for (int row = 0; row < screenY; ++row) {
                for (int col = 0; col < screenX; ++col) {
                    Palette.GreyShades shade = pixels[row][col];
                    if (shade == null)
                        shade = Palette.GreyShades.TRANSPARENT;
                    switch (shade) {
                        case WHITE:
                            g.setColor(Color.WHITE);
                            break;
                        case LIGHT_GREY:
                            g.setColor(Color.lightGray);
                            break;
                        case DARK_GREY:
                            g.setColor(Color.darkGray);
                            break;
                        case BLACK:
                            g.setColor(Color.black);
                            break;

                        case TRANSPARENT:
                            g.setColor(Color.MAGENTA);
                            break;
                    }

                    g.fillRect(col * pixelSize, row * pixelSize, pixelSize,
                               pixelSize);
                }
            }
        }

    }


}
