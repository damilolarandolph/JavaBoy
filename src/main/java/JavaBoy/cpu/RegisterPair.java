package JavaBoy.cpu;

public class RegisterPair {
   private final REGISTERS highREGISTERS;
    private final REGISTERS lowREGISTERS;

    public RegisterPair(REGISTERS highREGISTERS, REGISTERS lowREGISTERS){
        this.highREGISTERS = highREGISTERS;
        this.lowREGISTERS = lowREGISTERS;
    }

    public REGISTERS getHighRegister(){
        return this.highREGISTERS;
    }
    public REGISTERS getLowRegister(){
        return this.lowREGISTERS;
    }

}
