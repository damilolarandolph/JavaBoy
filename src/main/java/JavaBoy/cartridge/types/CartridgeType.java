package JavaBoy.cartridge.types;

import JavaBoy.memory.MemorySlot;

public abstract class  CartridgeType implements MemorySlot {


  protected byte[] data;

  CartridgeType(byte[] data){
    this.data = data;
  }

}
