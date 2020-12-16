package JavaBoy.video.pixelpipeline;

import JavaBoy.video.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FIFOFetcher {

    final PixelFIFO oamFifo;
    final PixelFIFO bgFifo;
    final Vram vram;
    final GpuRegisters gpuRegisters;
    final LCDC lcdc;
    int currentX = -1;
    int currentY = -1;
    private Pixel[] pixelLine = new Pixel[8];
    private ArrayList<SpriteAttribute> spriteAttributes;
    private FetcherStages currentStage = FetcherStages.GET_TILE;
    private FetchTypes currentType;
    private int bgOffsetX = 0;
    private int bgOffsetY = 0;
    private boolean requestedSpriteFetch = false;
    private SpriteAttribute nextSprite;
    private int cycles = 0;


    public FIFOFetcher(PixelFIFO oamFifo,
                       PixelFIFO bgFifo,
                       Vram vram,
                       LCDC lcdc,
                       GpuRegisters gpuRegisters
    ) {
        this.oamFifo = oamFifo;
        this.bgFifo = bgFifo;
        this.vram = vram;
        this.lcdc = lcdc;
        this.gpuRegisters = gpuRegisters;
    }


    public void tick() {
        ++cycles;
        switch (currentStage) {
            case GET_TILE:
            case GET_TILE_LOW:
                if (cycles == 2) {
                    this.cycles = 0;
                    moveToNextStage();
                }
                break;
            case SLEEP:
                pushPixels();
                if (cycles == 2) {
                    this.cycles = 0;
                    moveToNextStage();
                }
                break;
            case GET_TILE_HIGH:
                if (cycles == 2) {
                    this.cycles = 0;
                    if (currentType != FetchTypes.SPRITE) {
                        Vram.Tile tile = vram.getTileBG(bgOffsetX, bgOffsetY,
                                                        currentType == FetchTypes.WINDOW
                                                                ?
                                                                lcdc.getWindowMap()
                                                                :
                                                                lcdc.getBGMap(),
                                                        lcdc.getBGWindowAddressing());

                        int[] line = tile.getLine(currentY % 8);
                        for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                            pixelLine[pixelIdx] = new Pixel(line[pixelIdx],
                                                     Palettes.BGB);
                        }
                        bgOffsetX+= 8;
                    }else{
                       Vram.Tile tile = vram.getTile(nextSprite.getTileNumber(), LCDC.AddressingModes.M8000);
                       int [] line = tile.getLine(currentY % 8);
                        for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                            pixelLine[pixelIdx] = new Pixel(line[pixelIdx],
                                                     nextSprite.getPalette(), nextSprite.isAboveBG());
                        }
                    }
                    pushPixels();
                    moveToNextStage();
                }
                break;
        }
    }

    private void pushPixels(){
        if (pixelLine[0] == null)
            return;
        if (currentType == FetchTypes.SPRITE && !oamFifo.canPush())
            return;
        if (currentType == FetchTypes.BACKGROUND && !bgFifo.canPush())
            return;
        if (currentType == FetchTypes.WINDOW && !bgFifo.canPush())
            return;

        if (currentType == FetchTypes.SPRITE) {
            oamFifo.overlay(pixelLine);
            this.requestedSpriteFetch = false;
            this.bgFifo.enablePopping();
        } else {
            bgFifo.push(pixelLine);
        }

        Arrays.fill(pixelLine, null);

        if (requestedSpriteFetch){
            this.currentType = FetchTypes.SPRITE;
        }
    }

    public void notifyFetcher(int x, int y) {
        // just started rendering or moved to next scanline
        if (y != currentY) {
            bgFifo.clear();
            oamFifo.clear();
            this.bgOffsetX = gpuRegisters.getScx() + x;
            this.bgOffsetY = gpuRegisters.getScy() + y;
            this.currentX = x;
            this.currentY = y;
            this.currentStage = FetcherStages.GET_TILE;
            this.currentType = FetchTypes.BACKGROUND;
            this.requestedSpriteFetch = false;
            this.cycles = 0;
        }
        if (requestedSpriteFetch)
            return;

        if (lcdc.isWindowEnabled() && currentType != FetchTypes.WINDOW) {
            if (gpuRegisters.getWy() >= y && x >= gpuRegisters.getWx() - 7) {
                if (currentType == FetchTypes.BACKGROUND){
                    this.bgOffsetX = 0;
                    if (gpuRegisters.getWx() < 7)
                        this.bgOffsetX = gpuRegisters.getWx();
                    this.currentX = x;
                    this.currentY = y;
                    this.bgOffsetY = 0;
                    this.cycles = 0;
                    this.bgFifo.clear();
                }

                this.currentStage = FetcherStages.GET_TILE;
                this.currentType = FetchTypes.WINDOW;
            }
        }
        SpriteAttribute foundSprite = checkForSpritesOnX(x);
        if (foundSprite != null) {
            this.nextSprite = foundSprite;
            this.requestedSpriteFetch = true;
            this.bgFifo.disablePopping();
        }
    }

    private SpriteAttribute checkForSpritesOnX(int x) {
        for (SpriteAttribute sprite : this.spriteAttributes) {
            if (sprite.getXPosition() - 8 == x) {
                spriteAttributes.remove(sprite);
                return sprite;
            }
        }
        return null;
    }


    public void setSprites(ArrayList<SpriteAttribute> sprites) {
        this.spriteAttributes = sprites;
    }

    public void setCurrentX(int x) {
        this.currentX = x;
    }

    public void setCurrentY(int y) {
        this.currentY = y;
    }

    private void moveToNextStage() {
        if (currentStage == FetcherStages.SLEEP) {
            this.currentStage = FetcherStages.GET_TILE;
            return;
        }
        for (FetcherStages stage : FetcherStages.values()) {
            if (stage.ordinal() == this.currentStage.ordinal() + 1) {
                this.currentStage = stage;
                return;
            }
        }
    }

    private enum FetcherStages {
        GET_TILE, GET_TILE_LOW, GET_TILE_HIGH, SLEEP
    }

    private enum FetchTypes {
        WINDOW, BACKGROUND, SPRITE
    }


}
