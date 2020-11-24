package JavaBoy.cpu.instructions.jumpconditions;

import JavaBoy.cpu.CPU;

public enum JumpConditions {
    NZ(new ZFlagNotSet()),
    Z(new ZFlagSet()),
    NC(new CFlagNotSet()),
    C(new CFlagSet());

    private final JumpCondition condition;

    JumpConditions(JumpCondition condition) {
        this.condition = condition;
    }

    public boolean test(CPU cpu) {
        return condition.test(cpu);
    }
}
