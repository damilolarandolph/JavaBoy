package JavaBoy.cpu;

import org.junit.Test;
import static org.junit.Assert.*;

public class GBRegistersTest {
    GBRegisters registers = new GBRegisters();

    @Test
    public void testSetRegister() {
    registers.setRegister(REGISTERS.A, 0xF);
assertEquals(registers.getRegister(REGISTERS.A), 0xF);
        registers.setRegister(REGISTERS.H, 0xA);
        assertEquals(registers.getRegister(REGISTERS.H), 0xA);
    }


   /* @Test
    public void testGetRegisterPair(){

        registers.setRegister(REGISTERS.A, );

        registers.setRegister(REGISTERS.H, 0x0A);
        assertEquals(registers.getRegister(REGISTERS.A), 0x0F);
      int val =  registers.getRegisterPair(REGISTERS.A, REGISTERS.H);
System.out.println(val);
      assertEquals(val, 0xFA);
    }*/

    @Test
    public void testSetRegisterPair(){
        registers.setRegisterPair(REGISTERS.A, REGISTERS.H, 0xAFF);
        assertEquals(registers.getRegister(REGISTERS.A), 0xA);
        int val =  registers.getRegisterPair(REGISTERS.A, REGISTERS.H);
        assertEquals(val, 0xAFF);
    }

}
