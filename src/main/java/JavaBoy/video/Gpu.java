package JavaBoy.video;

import JavaBoy.cpu.interrupts.InterruptManager;
import JavaBoy.cpu.interrupts.Interrupts;
import JavaBoy.memory.MemorySlot;
import JavaBoy.video.pixelpipeline.FIFOFetcher;
import JavaBoy.video.pixelpipeline.Pixel;
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
    private int ticks = 0;
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
    }


    public void tick() {
        boolean newCycle = false;
        if (!lcdc.isLCD())
            return;
        ++ticks;
        if (ticks > 4) {
            ++cycles;
            newCycle = true;
            ticks = 0;
        }
        if (!newCycle)
            return;

        if (currentPhase == Phases.OAM_SCAN) {
            if (cycles == 1)
                lcdStat.setMode(LCDStat.Modes.OAM);
            if (cycles == 80) {
                this.fetcher.setSprites(oam.getSprites(currentY));
                this.currentPhase = Phases.PIXEL_TRANSFER;
            }
        } else if (currentPhase == Phases.PIXEL_TRANSFER) {
            if (currentX < 160) {
                fetcher.notifyFetcher(currentX, currentY);
                fetcher.tick();
                if (bgFifo.canPop()) {
                    display.renderPixel(mixFifo());
                    ++currentX;
                }
            } else {
                currentPhase = Phases.HBLANK;
                lcdStat.setMode(LCDStat.Modes.H_BLANK);
            }
        } else if (currentPhase == Phases.HBLANK) {
            if (456 - cycles == 0) {
                this.display.hBlank();
                currentX = 0;
                cycles = 0;
                this.currentPhase = Phases.OAM_SCAN;
                lcdStat.setMode(LCDStat.Modes.OAM);
                ++currentY;
            }
        }else if (currentPhase == Phases.VBLANK){
            if ((cycles / 456) == 10) {
                this.display.vBlank();
                currentY = 0;
                cycles = 0;
                this.currentPhase = Phases.OAM_SCAN;
                lcdStat.setMode(LCDStat.Modes.OAM);
            } else if ((cycles % 456) == 0) {
                ++currentY;
            }
        }

        if (currentY == 144) {
            if (lcdStat.isvBlankInterrupt()) {
                interruptManager.requestInterrupt(Interrupts.V_BLANK);
            }
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
        )
            interruptManager.requestInterrupt(Interrupts.LCD_STAT);
    }

    private Palette.GreyShades mixFifo() {
        Pixel bgPixel = bgFifo.getPixel();

        Palette.GreyShades bgShade = palette.getPaletteShade(
                lcdc.isPriority() ? bgPixel.getColour() : 0,
                bgPixel.getPalette());

        if (lcdc.isOBJEnable() && oamFifo.canPop()) {
            Pixel oamPixel = oamFifo.getPixel();
            Palette.GreyShades oamShade = palette.getPaletteShade(
                    oamPixel.getColour(), oamPixel.getPalette());
            if (oamPixel.getAboveBG()) {
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
