package JavaBoy.cpu;




public interface Registers {

     int getRegister(REGISTERS register);
     void setRegister(REGISTERS register, int value);
     int getRegisterPair(REGISTERS highRegister, REGISTERS lowRegister);
     void setRegisterPair(REGISTERS highRegister, REGISTERS lowRegister, int value);


}


enum REGISTERS{
A, B, C, D, E, F, H, L, SP, PC
}
