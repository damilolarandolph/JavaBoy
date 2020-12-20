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
    private final Pixel[] pixelLine = {
            new Pixel(),
            new Pixel(),
            new Pixel(),
            new Pixel(),
            new Pixel(),
            new Pixel(),
            new Pixel(),
            new Pixel()
    };
    private boolean requestedPush = false;
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
                        int[] line = vram.getTileLineBG(bgOffsetX, bgOffsetY,
                                                        currentType == FetchTypes.WINDOW
                                                                ?
                                                                lcdc.getWindowMap()
                                                                :
                                                                lcdc.getBGMap(),
                                                        lcdc.getBGWindowAddressing());

                        for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                            pixelLine[pixelIdx].setColour(line[pixelIdx]);
                            pixelLine[pixelIdx].setPalette(Palettes.BGB);
                        }
                        bgOffsetX+= 8;
                    }else{
                       int [] line = vram.getTile(nextSprite.getTileNumber(), currentY % 8, LCDC.AddressingModes.M8000);
                        for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                            pixelLine[pixelIdx].setColour(line[pixelIdx]);
                            pixelLine[pixelIdx].setPalette(nextSprite.getPalette());
                            pixelLine[pixelIdx].setAboveBG(nextSprite.isAboveBG());
                        }
                    }
                    requestedPush = true;
                    pushPixels();
                    moveToNextStage();
                }
                break;
        }
    }

    private void pushPixels(){
        if (!requestedPush)
            return;
        if (currentType == FetchTypes.SPRITE && !oamFifo.canPush())
            return;
        if (currentType == FetchTypes.BACKGROUND && !bgFifo.canPush())
            return;
        if (currentType == FetchTypes.WINDOW && !bgFifo.canPush())
            return;

        if (currentType == FetchTypes.SPRITE) {
            oamFifo.pushOverlay(pixelLine);
            this.requestedSpriteFetch = false;
            this.bgFifo.enablePopping();
        } else {
            bgFifo.push(pixelLine);
        }
       requestedPush = false;
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

        for (int idx = 0; idx < spriteAttributes.size(); ++idx){
            if (spriteAttributes.get(idx).getXPosition() - 8 == x) {
                return spriteAttributes.get(idx);
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
        switch (currentStage){
            case GET_TILE:
                this.currentStage = FetcherStages.GET_TILE_LOW;
                break;
            case GET_TILE_LOW:
                this.currentStage = FetcherStages.GET_TILE_HIGH;
                break;
            case GET_TILE_HIGH:
                this.currentStage = FetcherStages.SLEEP;
                break;
            case SLEEP:
                this.currentStage = FetcherStages.GET_TILE;
                break;
        }
    }

    private enum FetcherStages {
        GET_TILE, GET_TILE_LOW, GET_TILE_HIGH, SLEEP
    }

    private enum FetchTypes {
        WINDOW, BACKGROUND, SPRITE
    }


}
