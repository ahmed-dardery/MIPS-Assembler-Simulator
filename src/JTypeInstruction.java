public class JTypeInstruction implements Instruction {
    enum JTypeNames {
        j(2), jal(3);

        private int opcode;

        JTypeNames(int opcode) {
            this.opcode = opcode;
        }
    }

    private JTypeNames command;
    private int addr;

    public JTypeInstruction(JTypeNames command, int arg1) {
        this.command = command;
        this.addr = arg1;
    }

    @Override
    public String getInstructionName() {
        return command.name();
    }

    @Override
    public int getIdentifier() {
        return 0;
    }

    @Override
    public int getOpCode() {
        return command.opcode;
    }

    @Override
    public String toAssembly() {
        //TODO: jump label
        return String.format("%s %d", getInstructionName(), addr);
    }

    @Override
    public String toMachineLanguage() {
        return Integer.toBinaryString(getOpCode() | (1 << 6)).substring(1) +
                Integer.toBinaryString(addr | (1 << 26)).substring(1);
    }
}
