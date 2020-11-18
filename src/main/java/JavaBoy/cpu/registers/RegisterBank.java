package JavaBoy.cpu.registers;

import JavaBoy.cpu.REGISTERS;
import JavaBoy.cpu.flags.FlagBank;

import java.util.HashMap;

import static JavaBoy.utils.BitUtils.getLsb;
import static JavaBoy.utils.BitUtils.getMsb;

public  class RegisterBank {

    private final HashMap<REGISTERS, Register> registers = new HashMap<>();
    private final FlagBank flags;
    private  final Register SP;
    private final Register PC;

    public RegisterBank(FlagBank flags, Register SP, Register PC){
        this.flags = flags;
        this.SP = SP;
        this.PC = PC;
        this.registers.put(REGISTERS.F, flags);
    }

   public FlagBank getFlags(){
     return this.flags;
    }

    public Register getSP(){
        return  this.SP;
    }

    public Register getPC(){
        return this.PC;
    }

   public void writeRegister(REGISTERS register, int value) {
        if (registers.containsKey(register)) {
            registers.get(register).write(value);
        } else {
            Register reg = new Register8();
            reg.write(value);
            this.registers.put(register, reg);
        }
    }

   public void writeRegister(RegisterPairs registerPair, int value) {
        writeRegister(registerPair.getHigh(), getMsb(value));
        writeRegister(registerPair.getLow(), getLsb(value));
    }

   public int readRegister(REGISTERS register) {
        if (this.registers.containsKey(register)) {
            return registers.get(register).read();
        } else {
            Register reg = new Register8();
            reg.write(0);
            this.registers.put(register, reg);
            return reg.read();
        }
    }

   public int readRegister(RegisterPairs registerPair) {
        int msb = readRegister(registerPair.getHigh());
        int lsb = readRegister(registerPair.getLow());
        return (msb << 8) | lsb;
    }
}
