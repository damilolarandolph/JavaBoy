package JavaBoy.cpu.registers;

import JavaBoy.cpu.REGISTERS;

public enum RegisterPairs {

    BC(REGISTERS.B, REGISTERS.C), DE(REGISTERS.D, REGISTERS.E), HL(REGISTERS.H, REGISTERS.L);

    private final REGISTERS high;
    private final REGISTERS low;

    RegisterPairs(REGISTERS high, REGISTERS low) {
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
