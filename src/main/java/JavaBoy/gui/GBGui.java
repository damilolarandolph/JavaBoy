package JavaBoy.gui;

import JavaBoy.input.Joypad;
import JavaBoy.video.Palette;
import JavaBoy.video.Renderer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class GBGui implements Renderer {


    final int pixelSize = 2;
    private final int screenX = 160;
    private final int screenY = 144;
    private final int[] rgbWriteBuffer;
    private final Joypad joypad;
    BufferedImage readBuffer;
    private int x = 0;
    private int y = 0;
    private long frameTime;
    private int frames = 0;
    private boolean newFrame = true;
    private Canvas screen;
    private final Color WHITE = new Color(224, 248, 208);
    private final Color LIGHT_GREY = new Color(136, 192,112);
    private final Color DARK_GREY = new Color(52,104,86);
    private final Color BLACK = new Color(8, 24, 32);

    public GBGui(Joypad joypad) {
        GraphicsEnvironment env =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();

        readBuffer = config.createCompatibleImage(screenX,
                                                  screenY,
                                                  Transparency.OPAQUE);
        rgbWriteBuffer = new int[screenY * screenX];
        this.joypad = joypad;
    }

    @Override
    public void requestRefresh() {

        if (newFrame) {
            frameTime = System.currentTimeMillis();
            newFrame = false;
        }
        if ((System.currentTimeMillis() - frameTime) >= 1000) {
            System.out.println("Screen FPS: " + frames);
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
                colour = WHITE;
                break;
            case LIGHT_GREY:
                colour = LIGHT_GREY;
                break;
            case DARK_GREY:
                colour = DARK_GREY;
                break;
            case BLACK:
                colour = BLACK;
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
    public void vBlank() {
        x = 0;
        y = 0;
        int[] data =
                ((DataBufferInt) readBuffer.getRaster().getDataBuffer()).getData();
        System.arraycopy(rgbWriteBuffer, 0, data, 0, data.length);
        ++frames;
    }


    public void show() {

        Frame frame = new Frame();
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


        screen.addKeyListener(new KeyAdapter() {
            private int currentKey;

            @Override
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode() == currentKey){
                  currentKey = -1;
                  switch (e.getKeyCode()) {
                      case KeyEvent.VK_UP:
                          joypad.buttonReleased(Joypad.Buttons.UP);
                          break;
                      case KeyEvent.VK_DOWN:
                          joypad.buttonReleased(Joypad.Buttons.DOWN);
                          break;
                      case KeyEvent.VK_LEFT:
                          joypad.buttonReleased(Joypad.Buttons.LEFT);
                          break;
                      case KeyEvent.VK_RIGHT:
                          joypad.buttonReleased(
                                  Joypad.Buttons.RIGHT);
                          break;

                      case KeyEvent.VK_ENTER:
                          joypad.buttonReleased(Joypad.Buttons.START);
                          break;
                      case KeyEvent.VK_A:
                          joypad.buttonReleased(Joypad.Buttons.BUTTON_A);
                          break;
                      case KeyEvent.VK_S:
                          joypad.buttonReleased(Joypad.Buttons.BUTTON_B);
                          break;
                      case KeyEvent.VK_W:
                          joypad.buttonReleased(Joypad.Buttons.SELECT);
                          break;
                  }
              }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != currentKey) {
                    currentKey = e.getKeyCode();
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            joypad.buttonPressed(Joypad.Buttons.UP);
                            break;
                        case KeyEvent.VK_DOWN:
                            joypad.buttonPressed(Joypad.Buttons.DOWN);
                            break;
                        case KeyEvent.VK_LEFT:
                            joypad.buttonPressed(Joypad.Buttons.LEFT);
                            break;
                        case KeyEvent.VK_RIGHT:
                            joypad.buttonPressed(
                                    Joypad.Buttons.RIGHT);
                            break;

                        case KeyEvent.VK_ENTER:
                            joypad.buttonPressed(Joypad.Buttons.START);
                            break;
                        case KeyEvent.VK_A:
                            joypad.buttonPressed(Joypad.Buttons.BUTTON_A);
                            break;
                        case KeyEvent.VK_S:
                            joypad.buttonPressed(Joypad.Buttons.BUTTON_B);
                            break;
                        case KeyEvent.VK_W:
                            joypad.buttonPressed(Joypad.Buttons.SELECT);
                            break;
                    }
                }
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
