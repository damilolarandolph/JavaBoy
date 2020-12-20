package JavaBoy.cartridge;

import JavaBoy.cartridge.types.CartridgeType;
import JavaBoy.cartridge.types.CartridgeTypes;
import JavaBoy.memory.MemorySlot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Cartridge implements MemorySlot {

    CartridgeType cartData;
    int cartRegister = 0;
    public Cartridge(File file) {

        byte[] rom = getFileArray(file);
        try {
            cartData = CartridgeTypes.ROM_ONLY.getCartClass().getConstructor(
                    byte[].class).newInstance(rom);
        } catch (Exception err) {
            System.out.println("Error loading cart " + err.getMessage());
        }

    }

    private byte[] getFileArray(File file) {
        try {

            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(file));
            return stream.readAllBytes();
        } catch (Exception err) {
            return null;
        }
    }


    @Override
    public int getByte(int address) {
        if (address == 0xff50){
           return cartRegister;
        }
        return cartData.getByte(address);
    }

    @Override
    public void setByte(int address, int value) {
        if (address == 0xff50){
            cartRegister =value;
            cartData.setBootRomMapped(value == 0);
        }else {
            cartData.setByte(address, value);
        }
    }


    @Override
    public boolean hasAddressInSlot(int address) {
        return address == 0xff50 ||   cartData.hasAddressInSlot(address);
    }

}
