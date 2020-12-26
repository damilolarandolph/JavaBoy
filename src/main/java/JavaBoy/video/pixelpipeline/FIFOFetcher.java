package JavaBoy.video.pixelpipeline;

import JavaBoy.video.*;

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
    private final Oam.SpriteAttribute[] oamSpritesBuffer;
    private FetcherStages currentStage = FetcherStages.GET_TILE;
    private FetchTypes currentType;
    private int bgOffsetX = 0;
    private int bgOffsetY = 0;
    private FetchTypes wasFetching;
    private boolean requestedSpriteFetch = false;
    private Oam.SpriteAttribute nextSprite;
    private int cycles = 0;


    public FIFOFetcher(PixelFIFO oamFifo,
                       PixelFIFO bgFifo,
                       Vram vram,
                       LCDC lcdc,
                       GpuRegisters gpuRegisters,
                       Oam.SpriteAttribute[] oamSpritesBuffer
    ) {
        this.oamFifo = oamFifo;
        this.bgFifo = bgFifo;
        this.oamSpritesBuffer = oamSpritesBuffer;
        this.vram = vram;
        this.lcdc = lcdc;
        this.gpuRegisters = gpuRegisters;
    }


    public void tick() {
        ++cycles;
        switch (currentStage) {
            case GET_TILE:
            case GET_TILE_LOW:
            case SLEEP:
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
                    }else{
                        int lineIndex =currentY - (nextSprite.getYPosition() - 16) ;
                        if (nextSprite.isYFlipped())
                            lineIndex = 7 - lineIndex;
                       int [] line = vram.getTile(nextSprite.getTileForLine(currentY), lineIndex, LCDC.AddressingModes.M8000);
                       if (!nextSprite.isXFlipped()) {
                           for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                               pixelLine[pixelIdx].setColour(line[pixelIdx]);
                               pixelLine[pixelIdx].setPalette(
                                       nextSprite.getPalette());
                               pixelLine[pixelIdx].setAboveBG(
                                       nextSprite.isAboveBG());
                           }
                       }else {
                           for (int pixelIdx = 0; pixelIdx < 8; ++pixelIdx) {
                               pixelLine[7 - pixelIdx].setColour(line[pixelIdx]);
                               pixelLine[7 - pixelIdx].setPalette(
                                       nextSprite.getPalette());
                               pixelLine[7 - pixelIdx].setAboveBG(
                                       nextSprite.isAboveBG());
                           }
                       }
                    }
                    requestedPush = true;
                    pushPixels();
                    moveToNextStage();
                }
                break;
            case PUSH:
                if (!requestedPush) {
                    moveToNextStage();
                }else {
                    pushPixels();
                }
                this.cycles = 0;
                break;
        }
    }

    private void pushPixels(){
        if (!requestedPush)
            return;
        if (currentType == FetchTypes.SPRITE && !oamFifo.canPush())
            return;
        if (currentType != FetchTypes.SPRITE && !bgFifo.canPush())
            return;

        if (currentType != FetchTypes.SPRITE){
            bgOffsetX+= 8;
            bgFifo.push(pixelLine);
        }else {
            oamFifo.pushOverlay(pixelLine);
            requestedSpriteFetch = false;
            this.currentType = wasFetching;
            this.bgFifo.enablePopping();
        }
       requestedPush = false;
        if (requestedSpriteFetch){
            this.wasFetching = this.currentType;
            this.currentType = FetchTypes.SPRITE;
        }
    }

    public void notifyFetcher(int x, int y) {

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
        Oam.SpriteAttribute foundSprite = checkForSpritesOnX(x);
        if (foundSprite != null && lcdc.isOBJEnable()) {
            this.nextSprite = foundSprite;
            this.requestedSpriteFetch = true;
            this.bgFifo.disablePopping();
            if (!bgFifo.canPush()){
                this.requestedPush = false;
                this.wasFetching = currentType;
                this.currentType = FetchTypes.SPRITE;
                this.currentStage = FetcherStages.GET_TILE;
                this.cycles = 0;
            }
        }
    }
    public void reset(int line){
        bgFifo.clear();
        oamFifo.clear();
        this.bgOffsetX = gpuRegisters.getScx() ;
        this.bgOffsetY = gpuRegisters.getScy() + line;
        this.currentX = 0;
        this.currentY = line;
        this.currentStage = FetcherStages.GET_TILE;
        this.currentType = FetchTypes.BACKGROUND;
        this.requestedSpriteFetch = false;
        this.requestedPush = false;
        this.cycles = 0;
    }
    private Oam.SpriteAttribute checkForSpritesOnX(int x) {

        for (int idx = 0; idx < oamSpritesBuffer.length; ++idx){
            if (oamSpritesBuffer[idx].isDirty())
                continue;
            if (oamSpritesBuffer[idx].getXPosition() - 8 == x) {
                return oamSpritesBuffer[idx];
            }
        }
        return null;
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
                this.currentStage = FetcherStages.PUSH;
                break;
            case PUSH:
                this.currentStage = FetcherStages.GET_TILE;
                break;
        }
    }

    private enum FetcherStages {
        GET_TILE, GET_TILE_LOW, GET_TILE_HIGH,PUSH, SLEEP
    }

    private enum FetchTypes {
        WINDOW, BACKGROUND, SPRITE
    }


}
