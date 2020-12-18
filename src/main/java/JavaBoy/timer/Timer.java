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
    int cyclesAfterOverflow = 0;
    int divCycles = 0;
    int timaCycles = 0;

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
        divCycles += 1;
        if (divCycles >= 256 / 4) {
            divCycles -= 256 / 4;
            if (div == 0xff) {
                div = 0x00;
            } else {
                div += 1;
            }
        }
    }

    private void incrementTIMA() {


        if (didOverflow && cyclesAfterOverflow < 1) {
            cyclesAfterOverflow += 1;
            tima = 0x0;
            return;
        } else if (didOverflow && cyclesAfterOverflow == 1) {
            tima = tma;
            interruptManager.requestInterrupt(Interrupts.TIMER);
            didOverflow = false;
            cyclesAfterOverflow = 0;
        }
        if (isTimerEnabled()) {
            timaCycles += 1;
            int addition = 0;
            if (timaCycles >= getClockSelect()) {
                addition = timaCycles / getClockSelect();
            }
            for (; addition > 0; --addition) {
                if (tima == 0xff) {
                    didOverflow = true;
                    break;
                }
                tima += 1;
                timaCycles -= getClockSelect();
            }
        }
    }

    private int getClockSelect() {
        int clockSelect = (getNthBit(1, tac) << 1) | getNthBit(0, tac);

        switch (clockSelect) {
            case 0x00:
                return 1024 / 4;
            case 0x01:
                return 16 / 4;
            case 0x02:
                return 64 / 4;
            default:
                return 256 / 4;
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
                divCycles = 0;
                timaCycles = 0;

                break;
            case 0xff05:
                tima = value;
                break;
            case 0xff06:
                tma = value;
                break;
            case 0xff07:
                if (isTimerEnabled()) {
                    if (getClockSelect() == 16 / 4) {
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
