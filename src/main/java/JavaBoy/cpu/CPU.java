package JavaBoy.cpu;

import JavaBoy.memory.MemoryMap;

public interface CPU {

    @Deprecated
    Registers getRegisters();

    @Deprecated
    MemoryMap getMemoryMap();

    @Deprecated
    void incrementPC();

    int readPC();

    int readWordPC();

    boolean isFlag(FLAG flag);

    void writeAddress(Address addr, int value);

    int readAddress(Address addr);

    int getSP();

    void pushSP(int value);

    int popSP();

    @Deprecated
    void incrementSP();

    @Deprecated
    void decrementSP();

    void writeRegister(REGISTER register, int value);

    int readRegister(REGISTER register);

    int readWordRegister(RegisterPair pair);

    int readWordRegister(REGISTER register);

    int readWordRegister(REGISTER reg, REGISTER reg2);

    void writeWordRegister(RegisterPair pair, int value);

    void writeWordRegister(REGISTER register, int value);

    void readWordRegister(REGISTER reg, REGISTER reg2, int value);

    int getFlag(FLAG flag);

    @Deprecated
    void writeFlag(FLAG flag, int value);

    @Deprecated
    void setFlag(FLAG flag);

    void setFlag(FLAG flag, boolean value);

    @Deprecated
    void resetFlag(FLAG flag);

    void enableInterrupts();

    void disableInterrupts();

    int getPC();

    void setPC(int word);
}
