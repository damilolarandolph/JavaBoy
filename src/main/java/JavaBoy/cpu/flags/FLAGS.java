package JavaBoy.cpu.flags;

public enum FLAGS {
    Z(7), C(4), N(6), H(5);

    private final int bitIndex;

    FLAGS(int index) {
        this.bitIndex = index;
    }

    public int getBitIndex() {
        return this.bitIndex;
    }
}
