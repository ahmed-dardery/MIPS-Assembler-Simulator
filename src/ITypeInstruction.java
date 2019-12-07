public class ITypeInstruction implements Instruction {
    enum Reg {
        RT_RS_IMM,
        RT_IMM_RS,
        RT_IMM,
        RS_RT_LABEL,
    }

    enum ITypeNames {
        beq(4, Reg.RS_RT_LABEL), bne(5, Reg.RS_RT_LABEL),

        addi(8, Reg.RT_RS_IMM), addiu(9, Reg.RT_RS_IMM),
        slti(10, Reg.RT_RS_IMM), sltiu(11, Reg.RT_RS_IMM),
        andi(12, Reg.RT_RS_IMM), ori(13, Reg.RT_RS_IMM), xori(14, Reg.RT_RS_IMM),

        lui(15, Reg.RT_IMM),

        lb(32), lh(33), lw(35), lbu(36), lhu(37),
        sb(40), sh(41), sw(43);

        private Reg decodeOrder;
        private int opcode;

        ITypeNames(int opcode) {
            this(opcode, Reg.RT_IMM_RS);
        }

        ITypeNames(int opcode, Reg decodeOrder) {
            this.opcode = opcode;
            this.decodeOrder = decodeOrder;
        }
    }

    private ITypeNames command;
    private int rs = 0, rt = 0;
    private short imm = 0;

    public ITypeInstruction(ITypeNames command, int arg1, int arg2, int arg3) {
        this.command = command;
        switch (command.decodeOrder) {
            case RT_IMM:
                rt = arg1;
                imm = (short) arg2;
                break;
            case RT_RS_IMM:
                rt = arg1;
                rs = arg2;
                imm = (short) arg3;
                break;
            case RT_IMM_RS:
                rt = arg1;
                imm = (short) arg2;
                rs = arg3;
                break;
            case RS_RT_LABEL:
                rs = arg1;
                rt = arg2;
                imm = (short) arg3;
                break;
        }
    }

    @Override
    public String getInstructionName() {
        return command.name();
    }

    @Override
    public int getIdentifier() {
        return getOpCode();
    }

    @Override
    public int getOpCode() {
        return command.opcode;
    }

    @Override
    public String toAssembly() {
        switch (command.decodeOrder) {
            case RT_IMM:
                return String.format("%s %s, %d",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(rt),
                        imm);
            case RT_RS_IMM:
                return String.format("%s %s, %s, %d",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(rt),
                        RegisterNames.getRegisterIdentifier(rs),
                        imm);
            case RT_IMM_RS:
                return String.format("%s %s, %d(%s)",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(rt),
                        imm,
                        RegisterNames.getRegisterIdentifier(rs));
            case RS_RT_LABEL:
                //TODO: change immediate to label
                return String.format("%s %s, %s, %d",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(rs),
                        RegisterNames.getRegisterIdentifier(rt),
                        imm);
            default:
                return null;
        }
    }

    @Override
    public String toMachineLanguage() {
        //op (6 bits), rs (5 bits), rt (5 bits), imm (16 bits)
        return Integer.toBinaryString(getOpCode() | (1 << 6)).substring(1) +
                Integer.toBinaryString(rs | (1 << 5)).substring(1) +
                Integer.toBinaryString(rt | (1 << 5)).substring(1) +
                Integer.toBinaryString((int) imm | (1 << 16)).substring(1);
    }
}
