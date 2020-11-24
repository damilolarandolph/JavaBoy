package JavaBoy.cpu.interrupts;

public enum Interrupts {
    V_BLANK(0, 0x40),
    LCD_STAT(1, 0x48),
    TIMER(2, 0x50),
    SERIAL(3, 0x58),
    JOYPAD(4, 0x60);
    private final int bitIndex;
    private final int interruptVector;

    Interrupts(int index, int interruptVector) {
        this.bitIndex = index;
        this.interruptVector = interruptVector;
    }

    public int getBitIndex() {
        return this.bitIndex;
    }

    public int getInterruptVector() {
        return this.interruptVector;
    }
}
