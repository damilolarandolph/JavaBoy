package JavaBoy.cpu;




public interface Registers {

     int getRegister(REGISTERS REGISTERS);
     void setRegister(REGISTERS REGISTERS, int value);
     int getRegisterPair(REGISTERS highREGISTERS, REGISTERS lowREGISTERS);

     int getRegisterPair(RegisterPair pair);
     void setRegisterPair(REGISTERS highREGISTERS, REGISTERS lowREGISTERS, int value);

     void setRegisterPair(RegisterPair pair, int value);


}


