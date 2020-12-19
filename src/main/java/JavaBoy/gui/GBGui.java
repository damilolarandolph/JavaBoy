package JavaBoy.gui;

import JavaBoy.video.Palette;
import JavaBoy.video.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class GBGui implements Renderer {


    final int pixelSize = 2;
    private final int screenX = 160;
    private final int screenY = 144;
    private final int[] rgbWriteBuffer;
    BufferedImage readBuffer;
    private int x = 0;
    private int y = 0;
    private Frame frame;
    private long frameTime;
    private int frames = 0;
    private boolean newFrame = true;
    private Canvas screen;

    public GBGui() {
        GraphicsEnvironment env =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();

        readBuffer = config.createCompatibleImage(screenX,
                                                  screenY,
                                                  Transparency.OPAQUE);
        rgbWriteBuffer = new int[screenY * screenX];
    }

    @Override
    public  void requestRefresh() {

        if (newFrame){
frameTime = System.currentTimeMillis();
newFrame = false;
        }
        if ((System.currentTimeMillis() - frameTime)  >= 1000){
            System.out.println("Screen FPS: " + frames );
            frames = 0;
            newFrame = true;
        }

        screen.repaint();
    }

    @Override
    public void renderPixel(Palette.GreyShades shade) {

        Color colour = SystemColor.MAGENTA;
        switch (shade) {
            case WHITE:
                colour = SystemColor.WHITE;
                break;
            case LIGHT_GREY:
                colour = SystemColor.lightGray;
                break;
            case DARK_GREY:
                colour = SystemColor.darkGray;
                break;
            case BLACK:
                colour = SystemColor.black;
                break;
            case TRANSPARENT:
                colour = SystemColor.MAGENTA;
                break;
        }
        rgbWriteBuffer[(y * screenX) + x] = colour.getRGB();
        ++x;
    }

    @Override
    public void hBlank() {
        x = 0;
        ++y;
        if (y == screenY)
            vBlank();
    }

    @Override
    public  void vBlank() {
        x = 0;
        y = 0;
        int[] data = ((DataBufferInt)readBuffer.getRaster().getDataBuffer()).getData();
       System.arraycopy(rgbWriteBuffer, 0, data, 0, data.length);
        ++frames;
    }


    public void show() {

        frame = new JFrame();
        frame.setTitle("JavaBoy !");
        frame.setSize(700, 700);
        screen = new GBScreen();
        screen.setSize(pixelSize * screenX, pixelSize * screenY);
        frame.add(screen);

        frame.pack();
        frame.setVisible(true);

        screen.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });



    }

    public class GBScreen extends Canvas {

        @Override
        public void paint(Graphics g) {
                g.drawImage(readBuffer, 0, 0, screenX * pixelSize,
                            screenY * pixelSize, null);


        }

        @Override
        public void update(Graphics g) {
 paint(g);

        }
    }


}
