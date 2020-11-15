package JavaBoy.cpu;

import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterPairs;

public interface CPU {

    int readPC();

    int readWordPC();

    boolean isFlag(FLAGS FLAGS);

    void writeAddress(int address, int value);

    int readAddress(int address);

    int getSP();

    void setSP(int val);

    void pushSP(int value);

    int popSP();

    void writeRegister(REGISTERS REGISTERS, int value);

    int readRegister(REGISTERS REGISTERS);

    int readWordRegister(RegisterPair pair);

    int readWordRegister(RegisterPairs pair);

    void writeWordRegister(RegisterPairs pair, int value);

    void readWordRegister(REGISTERS reg, REGISTERS reg2, int value);

    int getFlag(FLAGS FLAGS);

    void setFlag(FLAGS FLAGS, boolean value);

    void enableInterrupts();

    void disableInterrupts();

    int getPC();

    void setPC(int word);
}
