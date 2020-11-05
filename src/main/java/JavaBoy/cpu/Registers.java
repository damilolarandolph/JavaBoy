package JavaBoy.cpu;




public interface Registers {

     int getRegister(REGISTER register);
     void setRegister(REGISTER register, int value);
     int getRegisterPair(REGISTER highRegister, REGISTER lowRegister);

     int getRegisterPair(RegisterPair pair);
     void setRegisterPair(REGISTER highRegister, REGISTER lowRegister, int value);

     void setRegisterPair(RegisterPair pair, int value);


}


