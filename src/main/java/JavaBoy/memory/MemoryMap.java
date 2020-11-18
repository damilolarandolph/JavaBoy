package JavaBoy.memory;

import java.util.ArrayList;
import java.util.Optional;

public class MemoryMap {

    private final MemorySlot[] slots;


    public MemoryMap(MemorySlot[] slots){
        this.slots = slots;
    }

    public int readByte(int address) {

        Optional<MemorySlot> slot = getSlot(address);

        if (slot.isPresent()) {
            return slot.get().getByte(address);
        } else {
            System.err.println("Address: " + Integer.toHexString(address) + " is not mapped to any slot");
            System.exit(1);
            return -99;

        }

    }

    public void setByte(int address, int value) {

        Optional<MemorySlot> slot = getSlot(address);

        if (slot.isPresent()) {
            slot.get().setByte(address, value);
        } else {
            System.err.println("Address: " + Integer.toHexString(address) + " is not mapped to any slot");
            System.exit(1);
        }

    }

    private Optional<MemorySlot> getSlot(int address) {
        for (MemorySlot slot : this.slots) {
            if (slot.hasAddressInSlot(address))
                return Optional.of(slot);
        }
        return Optional.empty();
    }


}
