package JavaBoy.input;

import JavaBoy.cpu.interrupts.InterruptManager;
import JavaBoy.cpu.interrupts.Interrupts;
import JavaBoy.memory.MemorySlot;
import JavaBoy.utils.BitUtils;

public class Joypad implements MemorySlot {

    final InterruptManager manager;
    private int directionBits = 0xf;
    private int buttonBits = 0xf;
    private int directionSelect = 1;
    private int buttonSelect = 1;


    public Joypad(InterruptManager interruptManager) {
        this.manager = interruptManager;
    }

    @Override
    public int getByte(int address) {
        return (2 << 6) | (buttonSelect << 5) |
                (directionSelect << 4) |
                (directionSelect == 0 ? directionBits : buttonBits);
    }

    @Override
    public void setByte(int address, int value) {
        buttonSelect = BitUtils.getNthBit(5, value);
        directionSelect = BitUtils.getNthBit(4, value);
    }

    public void buttonPressed(Buttons button) {
        System.out.println(button + " was pressed");
        int result = BitUtils.setNthBit(button.getIndex(), 0,
                                        button.isDirectional() ?
                                                directionBits : buttonBits);

        if (button.isDirectional()) {
            directionBits = result;
        } else {
            buttonBits = result;
        }

        if ((button.isDirectional && buttonSelect == 0)
                || (!button.isDirectional && directionBits == 0))
            manager.requestInterrupt(Interrupts.JOYPAD);
    }

    public void buttonReleased(Buttons button) {
        System.out.println(button + " was released");
        int result = BitUtils.setNthBit(button.getIndex(), 1,
                                        button.isDirectional() ?
                                                directionBits : buttonBits);
        if (button.isDirectional()) {
            directionBits = result;
        } else {
            buttonBits = result;
        }
    }

    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xff00;
    }

    public enum Buttons {
        BUTTON_A(0),
        BUTTON_B(1),
        SELECT(2),
        START(3),
        RIGHT(0, true),
        LEFT(1, true),
        DOWN(3, true),
        UP(2, true);

        private final int index;
        private final boolean isDirectional;

        Buttons(int index, boolean isDirectional) {
            this.index = index;
            this.isDirectional = isDirectional;
        }

        Buttons(int index) {
            this(index, false);
        }

        public boolean isDirectional() {
            return isDirectional;
        }

        public int getIndex() {
            return this.index;
        }
    }


}
