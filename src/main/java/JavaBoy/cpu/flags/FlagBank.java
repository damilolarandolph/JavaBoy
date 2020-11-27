package JavaBoy.cpu.flags;

import JavaBoy.cpu.registers.Register;

import static JavaBoy.utils.BitUtils.getNthBit;
import static JavaBoy.utils.BitUtils.setNthBit;


public class FlagBank implements Register {

    private int flags = 0;


    public void setFlag(FLAGS flag, boolean val) {
        flags = setNthBit(flag.getBitIndex(), val ? 1 : 0, flags);
    }

    public boolean isFlag(FLAGS flag) {
        int val = getNthBit(flag.getBitIndex(), flags);
        return val == 1;
    }

    public int getFlag(FLAGS flag) {
        return getNthBit(flag.getBitIndex(), flags);
    }


    @Override
    public int read() {
        return this.flags;
    }

    @Override
    public void write(int value) {

        this.flags = (value & 0xf0)   ;
    }

    @Override
    public void increment() {
        this.flags = (flags + 1) & 0xff;
    }

    @Override
    public void decrement() {
        this.flags = (flags - 1) & 0xff;
    }
}
