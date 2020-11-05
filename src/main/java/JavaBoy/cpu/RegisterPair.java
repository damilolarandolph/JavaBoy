package JavaBoy.cpu;

public class RegisterPair {
   private final REGISTER highRegister;
    private final REGISTER lowRegister;

    public RegisterPair(REGISTER highRegister, REGISTER lowRegister){
        this.highRegister = highRegister;
        this.lowRegister = lowRegister;
    }

    public REGISTER getHighRegister(){
        return this.highRegister;
    }
    public REGISTER getLowRegister(){
        return this.lowRegister;
    }

}
