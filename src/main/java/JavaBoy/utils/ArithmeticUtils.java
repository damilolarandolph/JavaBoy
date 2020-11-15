package JavaBoy.utils;

public class ArithmeticUtils {

    public static boolean isHalfCarry16(int val1, int val2){
        return (((val1 & 0xfff) + (val2 & 0xfff)) & 0x1000) == 0x1000;
    }

    public static boolean isCarry16(int val1, int val2){
        return (val1 + val2) >  0xffff;
    }
}
