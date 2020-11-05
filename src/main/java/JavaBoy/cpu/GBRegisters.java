package JavaBoy.cpu;

import java.util.HashMap;

public class GBRegisters implements Registers {

    private final HashMap<REGISTER, Integer> registersState = new HashMap<>();

    @Override
    public int getRegister(REGISTER register) {
        return this.registersState.get(register);
    }

    @Override
    public void setRegister(REGISTER register, int value) {
        this.registersState.put(register, value);
    }

    @Override
    public int getRegisterPair(REGISTER highRegister, REGISTER lowRegister) {
        int result = 0;
        result = result | this.registersState.get(highRegister);
        result = result << 8;
        result = result | this.registersState.get(lowRegister);
        return result;
    }

    @Override
    public void setRegisterPair(REGISTER highRegister, REGISTER lowRegister, int value) {

        int highBits = value >>> 8;

        this.registersState.put(highRegister, highBits);

        int lowBits = value & 0xFF;

        this.registersState.put(lowRegister, lowBits);

    }

    @Override
    public int getRegisterPair(RegisterPair pair) {
        return getRegisterPair(pair.getHighRegister(), pair.getLowRegister());
    }

    @Override
    public void setRegisterPair(RegisterPair pair, int value) {
        setRegisterPair(pair.getLowRegister(), pair.getLowRegister(), value);
    }
}
