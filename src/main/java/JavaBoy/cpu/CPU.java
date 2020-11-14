package JavaBoy.cpu;

import JavaBoy.cpu.flags.FLAGS;
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

    boolean isFlag(FLAGS FLAGS);

    void writeAddress(Address addr, int value);

    int readAddress(Address addr);

    int getSP();

    void pushSP(int value);

    int popSP();

    @Deprecated
    void incrementSP();

    @Deprecated
    void decrementSP();

    void writeRegister(REGISTERS REGISTERS, int value);

    int readRegister(REGISTERS REGISTERS);

    int readWordRegister(RegisterPair pair);

    int readWordRegister(REGISTERS REGISTERS);

    int readWordRegister(REGISTERS reg, REGISTERS reg2);

    void writeWordRegister(RegisterPair pair, int value);

    void writeWordRegister(REGISTERS REGISTERS, int value);

    void readWordRegister(REGISTERS reg, REGISTERS reg2, int value);

    int getFlag(FLAGS FLAGS);

    @Deprecated
    void writeFlag(FLAGS FLAGS, int value);

    @Deprecated
    void setFlag(FLAGS FLAGS);

    void setFlag(FLAGS FLAGS, boolean value);

    @Deprecated
    void resetFlag(FLAGS FLAGS);

    void enableInterrupts();

    void disableInterrupts();

    int getPC();

    void setPC(int word);
}
