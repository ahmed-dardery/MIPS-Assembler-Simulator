

import java.util.List;

public class Simulator {
    private List<Instruction> instructions;
    private int programCounter; // Acts like program counter but uses index in the list | -1 -> finish
    private static final int MEMORY_SIZE = (1 << 15), REGISTER_SIZE = (1 << 5);
    private Memory memory;
    private Memory text;
    private Memory registers;
    private int hi = 0;
    private int lo = 0;

    Simulator(List<Instruction> instructions) {
        this.instructions = instructions;
        this.memory = new Memory(MEMORY_SIZE);
        this.text = new Memory(MEMORY_SIZE);
        this.registers = new Memory(REGISTER_SIZE);

        this.memory.fillMemoryWithInstructions(instructions);
    }

    public boolean executeNextInstruction() throws Exception {
        Instruction current = fetchNextInstruction();
        System.out.println(current.toAssembly());
        Instruction.instructionType currentInstructionType = current.getInstructionType();
        advancePC();

        switch (currentInstructionType) {
            case RTypeInstruction:
                return RInstructionExecute(current);
            case ITypeInstruction:
                return IInstructionExecute(current);
            case JTypeInstruction:
                return JInstructionExecute(current);
            default:
                return false;
        }
    }

    public boolean executeAllInstructions() throws Exception {
        while (executeNextInstruction()) {
            if (programCounter == -1) return true; // end of the program
        }
        return false;
    }

    private Instruction fetchNextInstruction() throws Exception {
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

    private void setPC(int value) {
        programCounter = value;
    }

    private void advancePC() {
        programCounter += 4;
    }

    private boolean RInstructionExecute(Instruction current) {
        final long castUInt = 0x00000000ffffffffL;
        RTypeInstruction command = (RTypeInstruction) current;
        int rd = getFromRegisters(command.getRD());
        int rt = getFromRegisters(command.getRT());
        int rs = getFromRegisters(command.getRS());
        int shamt = command.getShamt();

        switch (command.getCommand()) {
            case add:
                try {
                    rd = Math.addExact(rs, rt);
                } catch (ArithmeticException e) {
                    System.out.println(e);
                }
                break;
            case sub:
                try {
                    rd = Math.subtractExact(rs, rt);
                } catch (ArithmeticException e) {
                    System.out.println(e);
                }
                break;
            case div:
                lo = rs / rt;
                hi = rs % rt;
                break;
            case mult:
                long temp = rs * rt;
                lo = (int) ((temp > (1 << 31)) ? (1 << 31) : temp);
                hi = (int) ((temp > (1 << 31)) ? temp - (1 << 31) : 0);
                break;
            case addu:
                rd = rs + rt;
                break;
            case subu:
                rd = rs - rt;
                break;
            case divu:
                lo = Integer.divideUnsigned(rs, rt);
                hi = Integer.remainderUnsigned(rs, rt);
                break;
            case multu:
                long tempu = (rs & castUInt) * (rt & castUInt);
                lo = (int) ((tempu > (1 << 31)) ? (1 << 31) : tempu);
                hi = (int) ((tempu > (1 << 31)) ? tempu - (1 << 31) : 0);
                break;
            case or:
                rd = rs | rt;
                break;
            case and:
                rd = rs & rt;
                break;
            case nor:
                rd = ~(rs | rt);
                break;
            case xor:
                rd = rs ^ rt;
                break;
            case sll:
                rd = rt << shamt;
                break;
            case srl:
                rd = rs >> shamt;
                break;
            case sra:
                rd = rs >>> shamt;
                break;
            case sllv:
                rd = rs << rt;
                break;
            case srav:
                rd = rs >>> rt;
                break;
            case srlv:
                rd = rs >> rt;
                break;
            case slt:
                rd = (rs < rt) ? 1 : 0;
                break;
            case sltu:
                rd = ((rs & castUInt) < (rt & castUInt)) ? 1 : 0;
                break;
            case jr:
                setPC(rs);
                return true;
            case jalr:
                setToRegister(getPC(), RegisterNames.getRegisterIndex("$ra"));
                setPC(rs);
                return true;
            case mfhi:
                rd = hi;
                break;
            case mflo:
                rd = lo;
                break;
            default:
                return false;
        }
        setToRegister(rd, command.getRD());
        return true;
    }

    private boolean IInstructionExecute(Instruction current) throws Exception {
        ITypeInstruction command = (ITypeInstruction) current;

        int RSValue = getFromRegisters(command.getRS());
        int RTValue = getFromRegisters(command.getRT());
        short imm = command.getImmediate();

        int Address = RSValue + imm;

        switch (command.getCommand()) {
            case lb:
                RTValue = memory.getByte(Address);
                break;
            case lbu:
                RTValue = Byte.toUnsignedInt(memory.getByte(Address));
                break;
            case lh:
                RTValue = memory.getHalfWord(Address);
                break;
            case lhu:
                RTValue = Short.toUnsignedInt(memory.getHalfWord((Address)));
                break;
            case lw:
                RTValue = memory.getWord(Address);
                break;
            case sb:
                memory.setByte(Address, (byte)RTValue);
                break;
            case sh:
                memory.setHalfWord(Address, (short)RTValue);
                break;
            case sw:
                memory.setWord(Address, RTValue);
                break;
            case lui:
                RTValue = imm << 16;
                break;
            case andi:
                RTValue = RSValue & imm;
                break;
            case ori:
                RTValue = RSValue | imm;
                break;
            case xori:
                RTValue = RSValue ^ imm;
                break;
            case addi:
            case addiu:
                RTValue = RSValue + imm;
                break;
            case sltiu:
            case slti:
                RTValue = RSValue < imm ? 1 : 0;
                break;
            case beq:
                if (RSValue == RTValue) setPC(BTA(imm));
                break;
            case bne:
                if (RSValue != RTValue) setPC(BTA(imm));
                break;
            default:
                return false;
        }

        setToRegister(RTValue, command.getRT()); // in case of jump RT will not change so it will set it's value with the same old value

        return true;
    }

    private boolean JInstructionExecute(Instruction current) {
        // TODO: execute all J type instructions here given an instruction of J type as a parameter and return false if you faced any error
        // TODO: Also update here the program counter
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
                return false;
        }
        return true;
    }

    public Memory getMemory() {
        return memory;
    }

    public Memory getText() {
        return text;
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
}
