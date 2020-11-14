package JavaBoy.cpu;

import java.util.HashMap;

public class GBRegisters implements Registers {

    private final HashMap<REGISTERS, Integer> registersState = new HashMap<>();

    @Override
    public int getRegister(REGISTERS REGISTERS) {
        return this.registersState.get(REGISTERS);
    }

    @Override
    public void setRegister(REGISTERS REGISTERS, int value) {
        this.registersState.put(REGISTERS, value);
    }

    @Override
    public int getRegisterPair(REGISTERS highREGISTERS, REGISTERS lowREGISTERS) {
        int result = 0;
        result = result | this.registersState.get(highREGISTERS);
        result = result << 8;
        result = result | this.registersState.get(lowREGISTERS);
        return result;
    }

    @Override
    public void setRegisterPair(REGISTERS highREGISTERS, REGISTERS lowREGISTERS, int value) {

        int highBits = value >>> 8;

        this.registersState.put(highREGISTERS, highBits);

        int lowBits = value & 0xFF;

        this.registersState.put(lowREGISTERS, lowBits);

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
