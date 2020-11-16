package JavaBoy.cartridge.types;

public enum CartridgeTypes  {
    ROM_ONLY(0x0, ROM.class);

    private final int code;
    private Class cartClass;

   <T extends CartridgeType> CartridgeTypes(int code, Class<T> cartClass ) {
        this.code = code;
        this.cartClass = cartClass;
    }

    public <T extends CartridgeType> Class<T> getCartClass(){
        return this.cartClass;
    }
    public int getCode() {
        return this.code;
    }
}
