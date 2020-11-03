package JavaBoy.cpu;

import java.util.HashMap;

public class GBRegisters implements Registers {

    private final HashMap<REGISTERS, Integer> registersState = new HashMap<>();

    @Override
    public int getRegister(REGISTERS register) {
        return this.registersState.get(register);
    }

    @Override
    public void setRegister(REGISTERS register, int value) {
       this.registersState.put(register, value);
    }

    @Override
    public int getRegisterPair(REGISTERS highRegister, REGISTERS lowRegister) {
     int result = 0;
     result = result | this.registersState.get(highRegister);
     result = result << 8;
     result = result | this.registersState.get(lowRegister);
     return result;
    }

    @Override
    public void setRegisterPair(REGISTERS highRegister, REGISTERS lowRegister, int value) {

        int highBits = value >>> 8;

        this.registersState.put(highRegister, highBits);

        int lowBits = value & 0xFF;

        this.registersState.put(lowRegister, lowBits);

    }
}
