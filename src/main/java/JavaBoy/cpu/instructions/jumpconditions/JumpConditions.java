package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;

public enum JumpConditions {
   NZ(new ZNotSet()), Z(new ZSet()), NC(new CYNotSet()), C(new CYSet());

    private final JumpCondition condition;

    public boolean test(CPU cpu) {
     return condition.test(cpu);
    }

    JumpConditions (JumpCondition condition){
     this.condition = condition;
    }
}
