package JavaBoy.cpu.registers;

public class Register16 implements Register {
    private int value;


    @Override
    public int read() {
        return this.value;
    }

    @Override
    public void write(int value) {
        this.value = value;
    }

    @Override
    public void increment() {
        this.value = (value + 1) & 0xffff;
    }

    @Override
    public void decrement() {

        this.value = (value - 1) & 0xffff;

    }
}
