package JavaBoy.utils;

public class ArithmeticUtils {

    public static boolean isHalfCarry16(int val1, int val2) {
        return (((val1 & 0xfff) + (val2 & 0xfff)) & 0x1000) == 0x1000;
    }

    public static boolean isCarry16(int val1, int val2) {
        return ((val1 & 0xffff) + (val2 & 0xffff)) > 0xffff;
    }

    public static boolean isHalfCarry8(int val1, int val2) {
        return (((val1 & 0xf) + (val2 & 0xf)) & 0x10) == 0x10;
    }

    public static boolean isCarry8(int val1, int val2) {
        return ((val1 & 0xff) + (val2 & 0xff)) > 0xff;
    }

}
