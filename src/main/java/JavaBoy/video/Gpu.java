package JavaBoy.video;

import JavaBoy.cpu.interrupts.InterruptManager;
import JavaBoy.cpu.interrupts.Interrupts;
import JavaBoy.memory.MemorySlot;
import JavaBoy.video.pixelpipeline.FIFOFetcher;
import JavaBoy.video.pixelpipeline.PixelFIFO;

public class Gpu implements MemorySlot {

    private final Renderer display;
    private final LCDC lcdc;
    private final LCDStat lcdStat;
    private final GpuRegisters gpuRegisters;
    private final Oam oam;
    private final InterruptManager interruptManager;
    private final Palette palette;
    private final Vram vram;
    private final FIFOFetcher fetcher;
    private final PixelFIFO oamFifo;
    private final PixelFIFO bgFifo;
    private int currentX = 0;
    private int currentY = 0;
    private Phases currentPhase = Phases.OAM_SCAN;
    private int cycles = 0;

    public Gpu(Renderer renderer, LCDC lcdc, LCDStat lcdStat,
               GpuRegisters gpuRegisters, Oam oam, Palette palette, Vram vram,
               FIFOFetcher fetcher, PixelFIFO oamFifo, PixelFIFO bgFifo,
               InterruptManager manager
    ) {
        this.display = renderer;
        this.lcdc = lcdc;
        this.lcdStat = lcdStat;
        this.interruptManager = manager;
        this.gpuRegisters = gpuRegisters;
        this.oam = oam;
        this.palette = palette;
        this.vram = vram;
        this.fetcher = fetcher;
        this.oamFifo = oamFifo;
        this.bgFifo = bgFifo;

        lcdStat.setMode(LCDStat.Modes.OAM);
    }

    public void refresh(){
        this.display.requestRefresh();
    }
    public void tick() {

        if (!lcdc.isLCD())
            return;

            ++cycles;

        if (currentPhase == Phases.OAM_SCAN) {
            if (cycles == 20) {
                this.fetcher.setSprites(oam.getSprites(currentY));
                this.currentPhase = Phases.PIXEL_TRANSFER;
            }
        } else if (currentPhase == Phases.PIXEL_TRANSFER) {
            if (currentX < 160) {
                for (int a = 4; a > 0; --a) {
                    fetcher.notifyFetcher(currentX, currentY);
                    fetcher.tick();
                    if (bgFifo.canPop()) {
                        display.renderPixel(mixFifo());
                        ++currentX;
                    }
                    if (currentX > 159)
                        break;
                }
            } else {
                currentPhase = Phases.HBLANK;
                lcdStat.setMode(LCDStat.Modes.H_BLANK);
            }
        } else if (currentPhase == Phases.HBLANK) {
            if (114 - cycles == 0) {
                this.display.hBlank();
                currentX = 0;
                cycles = 0;
                this.currentPhase = Phases.OAM_SCAN;
                lcdStat.setMode(LCDStat.Modes.OAM);
                ++currentY;
            }
        }else if (currentPhase == Phases.VBLANK){
            if ((cycles / 114) > 10) {
                currentY = 0;
                cycles = 0;
                this.currentPhase = Phases.OAM_SCAN;
                lcdStat.setMode(LCDStat.Modes.OAM);
            } else if ((cycles % 114) == 0) {
                ++currentY;
            }
        }

        if (currentY == 144) {
            interruptManager.requestInterrupt(Interrupts.V_BLANK);
            this.currentPhase = Phases.VBLANK;
            lcdStat.setMode(LCDStat.Modes.V_BLANK);
        }
        gpuRegisters.setLy(currentY);
        lcdStat.setCoincidenceFlag(
                gpuRegisters.getLy() == gpuRegisters.getLyc());

        checkStatInterrupts();


    }


    private void checkStatInterrupts() {
        if ((currentPhase == Phases.OAM_SCAN && lcdStat.isOAMInterrupt())
                || (currentPhase == Phases.HBLANK && lcdStat.ishBlankInterrupt())
                || (lcdStat.isCoincidenceInterrupt() && lcdStat.isCoincidenceFlag())
                || (lcdStat.isvBlankInterrupt() && currentPhase == Phases.VBLANK)
        )
            interruptManager.requestInterrupt(Interrupts.LCD_STAT);
    }

    private Palette.GreyShades mixFifo() {
        Palette.GreyShades bgShade;

            if (lcdc.isPriority()) {
                bgShade = bgFifo.getPixel();
            } else {
                bgShade = palette.getPaletteShade(0, bgFifo.peekPalette());
                bgFifo.getPixel();

            }

        if (lcdc.isOBJEnable() && oamFifo.canPop()) {
            if (oamFifo.peekIsAboveBG()) {
                Palette.GreyShades oamShade = oamFifo.getPixel();
                if (oamShade != Palette.GreyShades.TRANSPARENT)
                    return oamShade;
            }
        }
        return bgShade;

    }


    @Override
    public int getByte(int address) {
        return getSlot(address).getByte(address);
    }

    @Override
    public void setByte(int address, int value) {
        getSlot(address).setByte(address, value);
    }

    private MemorySlot getSlot(int address) {
        if (lcdc.hasAddressInSlot(address))
            return this.lcdc;
        if (oam.hasAddressInSlot(address))
            return this.oam;
        if (lcdStat.hasAddressInSlot(address))
            return lcdStat;
        if (gpuRegisters.hasAddressInSlot(address))
            return gpuRegisters;
        if (vram.hasAddressInSlot(address))
            return vram;
        if (palette.hasAddressInSlot(address))
            return palette;

        return null;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return getSlot(address) != null;
    }

    public enum Phases {
        OAM_SCAN, PIXEL_TRANSFER, HBLANK, VBLANK

    }
}
