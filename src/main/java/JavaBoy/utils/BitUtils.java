package JavaBoy.utils;

public class BitUtils {

    public static int getNthBit(int position,  int value){
       return (value >>> position) & 0x01;
    }

    public static int setNthBit(int position, int setBit, int bits){
        int bitMask = ~(0x01 << position);
        int result = (bits & bitMask) | (setBit << position);
        return result;
    }

    public static int getLsb(int value){
        return (value & 0xff);
    }

    public static int getMsb(int value){
       return (value >>> 8);
    }
}
