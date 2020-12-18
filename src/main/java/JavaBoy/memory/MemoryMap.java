package JavaBoy.memory;

import java.util.ArrayList;

public class MemoryMap {

    private MemorySlot[] slots;



    public void setSlots(MemorySlot[] slots) {
        this.slots = slots;
    }

    public int readByte(int address) {

        MemorySlot slot = getSlot(address);

        if (slot != null) {
            return slot.getByte(address);
        } else {
            System.err.println("Address: " + Integer.toHexString(
                    address) + " is not mapped to any slot");
            System.exit(1);
            return -99;

        }

    }

    public void setByte(int address, int value) {

        MemorySlot slot = getSlot(address);

        if (slot != null) {
            slot.setByte(address, value);
        } else {
            System.err.println("Address: " + Integer.toHexString(
                    address) + " is not mapped to any slot");
            System.exit(1);
        }

    }

    private MemorySlot getSlot(int address) {
       MemorySlot result = null;
       int size = slots.length;
        for (int idx = 0; idx < size; ++idx) {
         if (slots[idx].hasAddressInSlot(address))
         {
             result = slots[idx];
             break;
         }
        }
        return result;
    }


}
