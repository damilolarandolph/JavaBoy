package JavaBoy.cpu;

public enum REGISTER_PAIRS {

    BC(REGISTERS.B, REGISTERS.C), DE(REGISTERS.D, REGISTERS.E), HL(REGISTERS.H, REGISTERS.L);

    private final REGISTERS high;
    private final REGISTERS low;

    REGISTER_PAIRS(REGISTERS high, REGISTERS low) {
        this.high = high;
        this.low = low;
    }

    public REGISTERS getHigh() {
        return this.high;
    }

    public REGISTERS getLow() {
        return this.low;
    }

}
