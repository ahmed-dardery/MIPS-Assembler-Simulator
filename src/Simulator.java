import java.util.List;

public class Simulator {
    private List<Instruction> instructions;
    private int programCounter;
    private static final int MEMORY_SIZE = (1 << 14), REGISTER_SIZE = (1 << 5);
    private Memory memory;
    private Memory registers;
    private int hi = 0;
    private int lo = 0;

    Simulator() {
        this.memory = new Memory(MEMORY_SIZE);
        this.registers = new Memory(REGISTER_SIZE);
    }


    public List<Instruction> getInstructionList() {
        return instructions;
    }

    public void setInstructionList(List<Instruction> instructions) throws Exception {
        this.instructions = instructions;
        setPC(0);
    }

    public void updateInstruction(int index, Instruction instr) {
        this.instructions.set(index, instr);
    }

    public void executeNextInstruction(UpdateHandler updateHandler) throws Exception {
        if (updateHandler == null) updateHandler = new UpdateHandler();
        Instruction current = fetchNextInstruction();

        Instruction.InstructionType currentInstructionType = current.getInstructionType();

        advancePC();

        switch (currentInstructionType) {
            case RTypeInstruction:
                RInstructionExecute(current, updateHandler);
                break;
            case ITypeInstruction:
                IInstructionExecute(current, updateHandler);
                break;
            case JTypeInstruction:
                JInstructionExecute(current, updateHandler);
                break;
            default:
                throw new Exception("Unknown Instruction type.");
        }
    }

    public void executeNextInstruction() throws Exception {
        executeNextInstruction(null);
    }

    public void executeAllInstructions() throws Exception {
        while (true) {
            executeNextInstruction();
        }
    }

    public Instruction fetchNextInstruction() throws Exception {
        if (programCounter >> 2 >= instructions.size())
            throw new Exception("terminated.");
        else
            return instructions.get(programCounter >> 2);
    }

    private int getFromRegisters(int registerIndex) {
        return registers.getValue(registerIndex);
    }

    private void setToRegister(int data, int registerIndex) {
        registers.setValue(registerIndex, data);
    }

    private int BTA(short imm) {
        return getPC() + (int) imm << 2;
    }

    private int JTA(int addr) {
        return addr << 2;
    }

    //Notice that Program Counter is NOT the index of the instruction to be executed, it is the address of memory
    //that the next instruction is at.
    public int getPC() {
        return programCounter;
    }

    public void setPC(int value) throws Exception {
        if((value % 4) != 0) throw new Exception("not word aligned.");
        programCounter = value;
    }

    private void advancePC() {
        programCounter += 4;
    }

    private void RInstructionExecute(Instruction current, UpdateHandler updateHandler) throws Exception {
        final long castUInt = 0x00000000ffffffffL;
        RTypeInstruction command = (RTypeInstruction) current;
        int RDval = getFromRegisters(command.getRD());
        int RTval = getFromRegisters(command.getRT());
        int RSval = getFromRegisters(command.getRS());
        int shamt = command.getShamt();

        switch (command.getCommand()) {
            case add:
                RDval = Math.addExact(RSval, RTval);
                break;
            case sub:
                RDval = Math.subtractExact(RSval, RTval);
                break;
            case div:
                updateHandler.scheduleLoHi();
                lo = RSval / RTval;
                hi = RSval % RTval;
                break;
            case mult:
                updateHandler.scheduleLoHi();
                long temp = RSval * RTval;
                lo = (int) ((temp > (1 << 31)) ? (1 << 31) : temp);
                hi = (int) ((temp > (1 << 31)) ? temp - (1 << 31) : 0);
                break;
            case addu:
                RDval = RSval + RTval;
                break;
            case subu:
                RDval = RSval - RTval;
                break;
            case divu:
                updateHandler.scheduleLoHi();
                lo = Integer.divideUnsigned(RSval, RTval);
                hi = Integer.remainderUnsigned(RSval, RTval);
                break;
            case multu:
                updateHandler.scheduleLoHi();
                long tempu = (RSval & castUInt) * (RTval & castUInt);
                lo = (int) ((tempu > (1 << 31)) ? (1 << 31) : tempu);
                hi = (int) ((tempu > (1 << 31)) ? tempu - (1 << 31) : 0);
                break;
            case or:
                RDval = RSval | RTval;
                break;
            case and:
                RDval = RSval & RTval;
                break;
            case nor:
                RDval = ~(RSval | RTval);
                break;
            case xor:
                RDval = RSval ^ RTval;
                break;
            case sll:
                RDval = RTval << shamt;
                break;
            case srl:
                RDval = RSval >> shamt;
                break;
            case sra:
                RDval = RSval >>> shamt;
                break;
            case sllv:
                RDval = RSval << RTval;
                break;
            case srav:
                RDval = RSval >>> RTval;
                break;
            case srlv:
                RDval = RSval >> RTval;
                break;
            case slt:
                RDval = (RSval < RTval) ? 1 : 0;
                break;
            case sltu:
                RDval = ((RSval & castUInt) < (RTval & castUInt)) ? 1 : 0;
                break;
            case jr:
                setPC(RSval);
                return;
            case jalr:
                setToRegister(getPC(), RegisterNames.getRegisterIndex("$ra"));
                setPC(RSval);
                return;
            case mfhi:
                RDval = hi;
                break;
            case mflo:
                RDval = lo;
                break;
            default:
                throw new Exception("Unknown R-Type Instruction.");
        }
        updateHandler.scheduleRegister(command.getRD());
        setToRegister(RDval, command.getRD());
    }

    private void IInstructionExecute(Instruction current, UpdateHandler updateHandler) throws Exception {
        ITypeInstruction command = (ITypeInstruction) current;

        int RSval = getFromRegisters(command.getRS());
        int RTval = getFromRegisters(command.getRT());
        short imm = command.getImmediate();

        int Address = RSval + imm;

        switch (command.getCommand()) {
            case lb:
                RTval = memory.getByte(Address);
                break;
            case lbu:
                RTval = Byte.toUnsignedInt(memory.getByte(Address));
                break;
            case lh:
                RTval = memory.getHalfWord(Address);
                break;
            case lhu:
                RTval = Short.toUnsignedInt(memory.getHalfWord((Address)));
                break;
            case lw:
                RTval = memory.getWord(Address);
                break;
            case sb:
                updateHandler.scheduleMemory(Address >> 2);
                memory.setByte(Address, (byte) RTval);
                break;
            case sh:
                updateHandler.scheduleMemory(Address >> 2);
                memory.setHalfWord(Address, (short) RTval);
                break;
            case sw:
                updateHandler.scheduleMemory(Address >> 2);
                memory.setWord(Address, RTval);
                break;
            case lui:
                RTval = imm << 16;
                break;
            case andi:
                RTval = RSval & imm;
                break;
            case ori:
                RTval = RSval | imm;
                break;
            case xori:
                RTval = RSval ^ imm;
                break;
            case addi:
            case addiu:
                RTval = RSval + imm;
                break;
            case sltiu:
            case slti:
                RTval = RSval < imm ? 1 : 0;
                break;
            case beq:
                if (RSval == RTval) setPC(BTA(imm));
                break;
            case bne:
                if (RSval != RTval) setPC(BTA(imm));
                break;
            default:
                throw new Exception("Unknown I-Type Instruction.");
        }
        updateHandler.scheduleRegister(command.getRT());

        setToRegister(RTval, command.getRT()); // in case of jump RT will not change so it will set it's value with the same old value
    }

    private void JInstructionExecute(Instruction current, UpdateHandler updateHandler) throws Exception {
        JTypeInstruction command = (JTypeInstruction) current;
        int addr = command.getAddr();

        switch (command.getCommand()) {
            case j:
                setPC(JTA(addr));
                break;
            case jal:
                setToRegister(getPC(), RegisterNames.getRegisterIndex("$ra"));
                setPC(JTA(addr));
                break;
            default:
                throw new Exception("Unknown J-Type Instruction.");
        }
    }

    public Memory getMemory() {
        return memory;
    }

    public Memory getRegisters() {
        return registers;
    }

    public int getHiRegister() {
        return hi;
    }

    public int getLoRegister() {
        return lo;
    }

    public void setHiRegister(int hi) {
        this.hi = hi;
    }

    public void setLoRegister(int lo) {
        this.lo = lo;
    }

    public Instruction getCurrentInstruction(){
        Instruction i;
        if(programCounter % 4 == 0)
            try {
                return instructions.get(programCounter >> 2);
            }catch (Exception e){}
        return null;
    }
}