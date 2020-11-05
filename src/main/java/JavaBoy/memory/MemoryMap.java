package JavaBoy.memory;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MemoryMap {

    protected ArrayList<MemorySlot> slots = new ArrayList<>();


    public void addSlot(MemorySlot slot){
        this.slots.add(slot);
    }

    public int readByte(int address)  {

        Optional<MemorySlot> slot = getSlot(address);

        if (slot.isPresent()){
            return slot.get().getByte(address);
        }else{
            System.err.println("Address: " + Integer.toHexString(address)  + " is not mapped to any slot");
            System.exit(1);
            return -99;

        }

    }


    public void setByte(int address, int value)  {

        Optional<MemorySlot> slot = getSlot(address);

        if (slot.isPresent()){
             slot.get().setByte(address, value );
        }else{
            System.err.println("Address: " + Integer.toHexString(address)  + " is not mapped to any slot");
            System.exit(1);
        }

    }

    private Optional<MemorySlot> getSlot(int address){
       for(MemorySlot slot: this.slots){
           if (slot.hasAddressInSlot(address))
               return Optional.of(slot);
       }
       return Optional.empty();
    }


}
