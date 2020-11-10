package JavaBoy.cpu;

import JavaBoy.memory.MemoryMap;

public interface CPU {

    @Deprecated
    Registers getRegisters();

    @Deprecated
    MemoryMap getMemoryMap();

    void incrementPC();

    int readByteFromPC();

    int readWordFromPC();

    int readSignedByteFromPC();

    int readSignedWordFromPC();

    void writeToAddress(Address addr, int value);

    int readFromAddress(Address addr);

    int getSP();

    void incrementSP();

    void decrementSP();

    void writeByteRegister(REGISTER register, int value);

    int readByteRegister(REGISTER register);

    int readWordRegister(RegisterPair pair);

    int readWordRegister(REGISTER register);

    int readWordRegister(REGISTER reg, REGISTER reg2);

    void writeWordRegister(RegisterPair pair, int value);

    void writeWordRegister(REGISTER register, int value);

    void readWordRegister(REGISTER reg, REGISTER reg2, int value);


    int getFlag(FLAG flag);

    void writeFlag(FLAG flag, int value);

    void setFlag(FLAG flag);

    void resetFlag(FLAG flag);


    int getPC();
}
