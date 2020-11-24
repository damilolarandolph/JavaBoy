package JavaBoy.cpu.interrupts;

import JavaBoy.cpu.CPU;
import JavaBoy.memory.MemorySlot;

import static JavaBoy.utils.BitUtils.*;

public class InterruptManager implements MemorySlot {

    private int interruptEnable = 0;
    private int interruptRequests = 0xe0;
    private boolean ime = true;
    private boolean imeRequest = false;

    public boolean handleInterrupts(CPU cpu) {

        if (imeRequest) {
            this.ime = true;
            imeRequest = false;
            return false;
        }

        if (ime) {
            for (Interrupts interrupt : Interrupts.values()) {
                if (getNthBit(interrupt.getBitIndex(), interruptRequests) != 1)
                    continue;
                if (getNthBit(interrupt.getBitIndex(), interruptEnable) != 1)
                    continue;
                ime = false;

                cpu.pushSP(getMsb(cpu.getPC()));
                cpu.pushSP(getLsb(cpu.getPC()));
                cpu.setPC(interrupt.getInterruptVector());
                unrequestInterrupt(interrupt);
                cpu.addCycles(5);
                return true;
            }
        }

        return false;
    }

    public boolean hasServiceableInterrupts() {
        return ((interruptRequests & 0x1f) & (interruptEnable & 0x1f)) != 0;
    }

    public boolean isMasterEnabled() {
        return ime;
    }

    public void enableInterrupts() {
        imeRequest = true;
    }

    public void disableInterrupts() {
        this.ime = false;
    }

    public void requestInterrupt(Interrupts interrupt) {
        interruptRequests = 0xe0 | setNthBit(interrupt.getBitIndex(), 1,
                                             interruptRequests);
    }

    private void unrequestInterrupt(Interrupts interrupts) {
        interruptRequests = 0xe0 | setNthBit(interrupts.getBitIndex(), 0,
                                             interruptRequests);
    }

    @Override
    public int getByte(int address) {
        if (address == 0xffff) {
            return this.interruptEnable & 0xff;
        } else {
            return this.interruptRequests & 0xff;
        }
    }

    @Override
    public void setByte(int address, int value) {
        if (address == 0xffff) {
            this.interruptEnable = value & 0xff;
        } else {
            this.interruptRequests = 0xe0 | (value & 0xff);
        }
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xffff || address == 0xff0f;
    }
}
