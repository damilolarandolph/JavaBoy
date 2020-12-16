package JavaBoy.gui;

import JavaBoy.video.Palette;
import JavaBoy.video.Renderer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class GBGui implements Renderer, Runnable {

    final int pixelSize = 2;
    private final int screenX = 160;
    private final int screenY = 144;
    BufferedImage readBuffer;
    BufferedImage writeBuffer;
    private boolean shouldUpdate = false;
    private int x = 0;
    private int y = 0;
    private Frame frame;
    private Canvas screen;

    public GBGui() {
        writeBuffer = new BufferedImage(screenX * pixelSize,
                                        screenY * pixelSize,
                                        BufferedImage.TYPE_INT_RGB);


    }

    @Override
    public void renderPixel(Palette.GreyShades shade) {
        var g = writeBuffer.getGraphics();
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
        g.fillRect(x * pixelSize, y * pixelSize, pixelSize,
                   pixelSize);
        ++x;
    }

    @Override
    public void hBlank() {
        x = 0;
        ++y;
    }

    @Override
    public synchronized void vBlank() {
        x = 0;
        y = 0;
        shouldUpdate = true;
        notifyAll();
    }


    public void show() {

        frame = new Frame();
        frame.setTitle("JavaBoy !");
        frame.setSize(new Dimension(600, 600));
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

    @Override
    public void run() {
        while (true) {
            while (!shouldUpdate) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException error) {
break;
                    }
                }
            }
            synchronized (this) {
                readBuffer = writeBuffer;
                screen.update(screen.getGraphics());
            }
        }
    }


    public class GBScreen extends Canvas {

        @Override
        public void paint(Graphics g) {
            update(g);
        }

        @Override
        public  void update(Graphics g){
               if (readBuffer != null) {
                   Graphics2D g2 = (Graphics2D) g;
                   g2.drawImage(readBuffer, 0, 0, null);
               }
        }
    }


}
