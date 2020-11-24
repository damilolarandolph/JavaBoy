package JavaBoy.timer;

import JavaBoy.cpu.interrupts.InterruptManager;
import JavaBoy.cpu.interrupts.Interrupts;
import JavaBoy.memory.MemorySlot;

import static JavaBoy.utils.BitUtils.getNthBit;

public class Timer implements MemorySlot {
    final InterruptManager interruptManager;
    int div = 0;
    int tima = 0;
    int tma = 0;
    int tac = 0;
    boolean didOverflow = false;
    int ticksAfterOverflow = 0;
    int divTicks = 0;
    int timaTicks = 0;

    public Timer(InterruptManager interruptManager) {
        this.interruptManager = interruptManager;
    }

    public void tick() {
        incrementDIV();
        incrementTIMA();

    }

    public int getTIMA() {
        return this.tima;
    }

    private boolean isTimerEnabled() {
        return getNthBit(2, tac) == 1;
    }


    private void incrementDIV() {
        divTicks += 1;
        if (divTicks >= 256) {
            divTicks -= 256;
            if (div == 0xff) {
                div = 0x00;
            } else {
                div += 1;
            }
        }
    }

    private void incrementTIMA() {


        if (didOverflow && ticksAfterOverflow < 4) {
            ticksAfterOverflow += 1;
            tima = 0x0;
            return;
        } else if (didOverflow && ticksAfterOverflow == 4) {
            tima = tma;
            interruptManager.requestInterrupt(Interrupts.TIMER);
            didOverflow = false;
            ticksAfterOverflow = 0;
        }
        if (isTimerEnabled()) {
            timaTicks += 1;
            int addition = 0;
            if (timaTicks >= getClockSelect()) {
                addition = timaTicks / getClockSelect();
            }
            for (; addition > 0; --addition) {
                if (tima == 0xff) {
                    didOverflow = true;
                    break;
                }
                tima += 1;
                timaTicks -= getClockSelect();
            }
        }
    }

    private int getClockSelect() {
        int clockSelect = (getNthBit(1, tac) << 1) | getNthBit(0, tac);

        switch (clockSelect) {
            case 0x00:
                return 1024;
            case 0x01:
                return 16;
            case 0x02:
                return 64;
            default:
                return 256;
        }
    }

    private int translateAddr(int addr) {
        return addr - 0xff04;
    }

    @Override
    public int getByte(int address) {
        switch (address) {
            case 0xff04:
                return div;
            case 0xff05:
                return tima;
            case 0xff06:
                return tma;
            default:
                return tac;
        }
    }

    @Override
    public void setByte(int address, int value) {
        switch (address) {
            case 0xff04:
                if (isTimerEnabled() && div == 1)
                    tima += 1;
                div = 0x0;
                divTicks = 0;
                timaTicks = 0;

                break;
            case 0xff05:
                tima = value;
                break;
            case 0xff06:
                tma = value;
                break;
            case 0xff07:
                if (isTimerEnabled()) {
                    if (getClockSelect() == 16) {
                        int newClock = (getNthBit(1, tac) << 1) | getNthBit(0,
                                                                            tac);
                        if (newClock == 0) {
                            tima += 1;
                            tima = 0;
                        }
                    }
                }
                tac = value;
                break;
        }
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address >= 0xff04 && address <= 0xff07;
    }
}
