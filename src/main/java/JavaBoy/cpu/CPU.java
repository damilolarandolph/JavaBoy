package JavaBoy.cpu;

import JavaBoy.cpu.flags.FLAGS;
import JavaBoy.cpu.registers.RegisterBank;
import JavaBoy.cpu.registers.RegisterPairs;

public abstract class CPU {


    abstract public int readPC();

    abstract public int readWordPC();

    abstract public boolean isFlag(FLAGS FLAGS);

    abstract public void writeAddress(int address, int value);

    abstract public int readAddress(int address);

    abstract public int getSP();

    abstract public void setSP(int val);

    abstract public void pushSP(int value);

    abstract public int popSP();

    abstract public void writeRegister(REGISTERS REGISTERS, int value);

    abstract public int readRegister(REGISTERS REGISTERS);

    abstract public int readWordRegister(RegisterPairs pair);

    abstract public void writeWordRegister(RegisterPairs pair, int value);

    abstract public void readWordRegister(REGISTERS reg, REGISTERS reg2, int value);

    abstract public int getFlag(FLAGS FLAGS);

    abstract public void setFlag(FLAGS FLAGS, boolean value);

    abstract public void enableInterrupts();

    abstract public void disableInterrupts();

    abstract public int getPC();

    abstract public void setPC(int word);
}
