package JavaBoy.cpu.registers;

public interface Register {
    int read();

    void write(int value);

    void increment();

    void decrement();
}
