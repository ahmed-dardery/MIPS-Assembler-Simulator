public class RTypeInstruction extends Instruction {

    enum Reg {
        RD_RT_RS,
        RD_RS_RT,
        RD_RT_SHAMT,
        RS_RT,
        RS,
        RD
    }
    enum RTypeNames {
        sll(0, Reg.RD_RT_SHAMT), srl(2, Reg.RD_RT_SHAMT), sra(3, Reg.RD_RT_SHAMT),

        sllv(4, Reg.RD_RT_RS), srlv(6, Reg.RD_RT_RS), srav(7, Reg.RD_RT_RS),

        jr(8, Reg.RS), jalr(9, Reg.RS),
        mfhi(16, Reg.RD), mflo(18, Reg.RD),
        mult(24, Reg.RS_RT), multu(25, Reg.RS_RT), div(26, Reg.RS_RT), divu(27, Reg.RS_RT),
        add(32), addu(33), sub(34), subu(35),
        and(36), or(37), xor(38), nor(39),
        slt(42), sltu(43);

        private int funct;
        private Reg decodeOrder;

        RTypeNames(int funct) {
            this(funct, Reg.RD_RS_RT);
        }

        RTypeNames(int funct, Reg decodeOrder) {
            this.funct = funct;
            this.decodeOrder = decodeOrder;
        }
    }

    private final RTypeNames command;
    private int rd = 0;
    private int rs = 0;
    private int rt = 0;
    private int shamt = 0;

    public RTypeInstruction(RTypeNames command, int arg1, int arg2, int arg3) {
        this.command = command;
        switch (command.decodeOrder) {
            case RD_RT_RS:
                rd = arg1;
                rt = arg2;
                rs = arg3;
                break;
            case RD_RT_SHAMT:
                rd = arg1;
                rt = arg2;
                shamt = arg3 & ((1 << 5) - 1);
                break;
            case RS:
                rs = arg1;
                break;
            case RD:
                rd = arg1;
                break;
            case RS_RT:
                rs = arg1;
                rt = arg2;
            case RD_RS_RT:
                rd = arg1;
                rs = arg2;
                rt = arg3;
                break;
        }
    }

    public RTypeNames getCommand() {
        return command;
    }

    public int getRD() {
        return rd;
    }

    public int getRS() {
        return rs;
    }

    public int getRT() {
        return rt;
    }

    public int getShamt() {
        return shamt;
    }

    public String getInstructionName() {
        return getCommand().name();
    }

    public int getIdentifier() {
        return getCommand().funct << 6;
    }

    public int getOpCode() {
        return 0;
    }

    public String toAssembly() {
        switch (getCommand().decodeOrder) {
            case RD_RT_SHAMT:
                return String.format("%s %s, %s, %d",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRD()),
                        RegisterNames.getRegisterIdentifier(getRS()),
                        getShamt());
            case RS:
                return String.format("%s %s",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRS()));
            case RD:
                return String.format("%s %s",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRD()));
            case RS_RT:
                return String.format("%s %s, %s",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRS()),
                        RegisterNames.getRegisterIdentifier(getRT()));
            case RD_RS_RT:
                return String.format("%s %s, %s, %s",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRD()),
                        RegisterNames.getRegisterIdentifier(getRS()),
                        RegisterNames.getRegisterIdentifier(getRT()));
            case RD_RT_RS:
                return String.format("%s %s, %s, %s",
                        getInstructionName(),
                        RegisterNames.getRegisterIdentifier(getRD()),
                        RegisterNames.getRegisterIdentifier(getRT()),
                        RegisterNames.getRegisterIdentifier(getRS()));
            default:
                return null;
        }
    }

    public String toMachineLanguage() {
        //op[zero] (6 bits), rs (5 bits), rt (5 bits), rd (5 bits) shamt (5 bits), funct (6 bits)
        return Integer.toBinaryString(getOpCode() | (1 << 6)).substring(1) +
                Integer.toBinaryString(getRS() | (1 << 5)).substring(1) +
                Integer.toBinaryString(getRT() | (1 << 5)).substring(1) +
                Integer.toBinaryString(getRD() | (1 << 5)).substring(1) +
                Integer.toBinaryString(getShamt() | (1 << 5)).substring(1) +
                Integer.toBinaryString(getCommand().funct | (1 << 5)).substring(1);
    }

	@Override
	public instructionType getInstructionType() {
		return Instruction.instructionType.RTypeInstruction;
	}
}
