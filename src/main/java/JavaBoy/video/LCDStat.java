package JavaBoy.video;

import JavaBoy.memory.MemorySlot;

import static JavaBoy.utils.BitUtils.getNthBit;

public class LCDStat implements MemorySlot {

    int data = 0;
    private boolean coincidenceInterrupt = false;
    private boolean OAMInterrupt = false;
    private boolean vBlankInterrupt = false;
    private boolean hBlankInterrupt = false;
    private boolean coincidenceFlag = false;
    private Modes modeFlag = Modes.H_BLANK;

    @Override
    public int getByte(int address) {
        return (coincidenceInterrupt ? 1 << 6 : 0)
                | (OAMInterrupt ? 1 << 5 : 0)
                | (vBlankInterrupt ? 1 << 4 : 0)
                | (hBlankInterrupt ? 1 << 3 : 0)
                | (coincidenceFlag ? 1 << 2 : 0)
                | (modeFlag.getValue());
    }

    @Override
    public void setByte(int address, int value) {
        value &= 0x7f;
        coincidenceInterrupt = getNthBit(6, value) == 1;
        OAMInterrupt = getNthBit(5, value) == 1;
        vBlankInterrupt = getNthBit(4, value) == 1;
        hBlankInterrupt = getNthBit(3, value) == 1;
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xff41;
    }

    public boolean isCoincidenceInterrupt() {
        return coincidenceInterrupt;
    }

    public void setCoincidenceInterrupt(boolean coincidenceInterrupt) {
        this.coincidenceInterrupt = coincidenceInterrupt;
    }

    public Modes getMode() {
        return this.modeFlag;
    }

    public void setMode(Modes mode) {
        this.modeFlag = mode;
    }

    public boolean isOAMInterrupt() {
        return OAMInterrupt;
    }

    public void setOAMInterrupt(boolean OAMInterrupt) {
        this.OAMInterrupt = OAMInterrupt;
    }

    public boolean isvBlankInterrupt() {
        return vBlankInterrupt;
    }

    public void setvBlankInterrupt(boolean vBlankInterrupt) {
        this.vBlankInterrupt = vBlankInterrupt;
    }

    public boolean ishBlankInterrupt() {
        return hBlankInterrupt;
    }

    public void sethBlankInterrupt(boolean hBlankInterrupt) {
        this.hBlankInterrupt = hBlankInterrupt;
    }

    public boolean isCoincidenceFlag() {
        return coincidenceFlag;
    }

    public void setCoincidenceFlag(boolean coincidenceFlag) {
        this.coincidenceFlag = coincidenceFlag;
    }

    public enum Modes {
        H_BLANK(0), V_BLANK(1), OAM(2), Transfer(3);

        private final int value;

        Modes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
