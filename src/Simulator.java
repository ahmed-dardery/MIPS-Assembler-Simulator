

import java.util.List;

public class Simulator {
    private List<Instruction> instructions;
    private int nextInstrcutionIdx; // Acts like program counter but uses index in the list | -1 -> finish
    private final int memoryOffset; // start of the program in the memory
    static final int MEMORY_SIZE = (1<<15),REGISTER_SIZE = (1<<5),BYTE = 0xFF,HALF_WORD = 0xFF,MEMORY_START = 0x00001000;
    private int[] memory , registers;

    Simulator(List<Instruction> instructions,int memoryOffset , int[] memory, int[] registers){
        this.instructions = instructions;
        this.memoryOffset = memoryOffset;
        this.memory = memory;
        this.registers = registers;

        fillMemoryWithInstructions();
    }
    Simulator(List<Instruction> instructions,int memoryOffset){
            this(instructions,memoryOffset,new int[MEMORY_SIZE],new int[REGISTER_SIZE]);
    }
    private void fillMemoryWithInstructions() {
        //@TODO: Put the instructions list in the memory
    }
    public boolean executeNextInstruction(){
        Instruction current = getNextInstruction();

        Instruction.instructionType currentInstructionType = current.getInstructionType();

        if (currentInstructionType == Instruction.instructionType.RTypeInstruction) return RInstructionExecute(current);
        else if (currentInstructionType == Instruction.instructionType.ITypeInstruction) return IInstructionExecute(current);
        else if (currentInstructionType == Instruction.instructionType.JTypeInstruction) return JInstructionExecute(current);

        return false;
    }
    public boolean executeAllInstructions(){
        while (executeNextInstruction()){
            if (nextInstrcutionIdx == -1) return true; // end of the program
        }
        return false;
    }
    public int getProgramCounter(){
        //TODO: find appropriate way to return the program counter given index in the list and offset in memory
        return 0;
    }
    private Instruction getNextInstruction(){
        // TODO: change here if you want it as stored program concept
        return instructions.get(nextInstrcutionIdx);
    }
    private boolean RInstructionExecute(Instruction current){
        // TODO: execute all r type instructions here given an instruction of R type as a parameter and return false if you faced any error
        // TODO: Also update here the program counter
        return true;
    }
    private boolean IInstructionExecute(Instruction current){
        // TODO: execute all I type instructions here given an instruction of I type as a parameter and return false if you faced any error
        // TODO: Also update here the program counter
        ITypeInstruction command = (ITypeInstruction) current;

        int RSValue = getFromRegisters(command.getRS());
        int RTValue = getFromRegisters(command.getRT());
        int imm = command.getImmediate();


        if (nextInstrcutionIdx++ >= instructions.size()) nextInstrcutionIdx = -1; // update the next instruction and if you reach the end make it -1

        switch (command.getCommand()){
            case lb:
            case lbu:
                RTValue = getFromMemory(imm) & BYTE;
                break;
            case lh:
            case lhu:
                RTValue = getFromMemory(imm) & HALF_WORD;
                break;
            case lw:
                RTValue = getFromMemory(imm);
                break;
            case sb:
                setToMemory(RTValue & BYTE,imm);
                break;
            case sh:
                setToMemory(RTValue & HALF_WORD,imm);
                break;
            case sw:
                setToMemory(RTValue,imm);
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
                RTValue = RSValue<imm?1:0;
                break;
            case beq:
                if (RSValue == RTValue) nextInstrcutionIdx = gotoLabel(imm);
                break;
            case bne:
                if (RSValue != RTValue) nextInstrcutionIdx = gotoLabel(imm);
                break;
            default:
                return false;
        }

        setToRegister(RTValue,command.getRT()); // in case of jump RT will not change so it will set it's value with the same old value
        return true;
    }
    private boolean JInstructionExecute(Instruction current){
        // TODO: execute all J type instructions here given an instruction of J type as a parameter and return false if you faced any error
        // TODO: Also update here the program counter
        JTypeInstruction command = (JTypeInstruction) current;
        int add = command.getAddr();

        switch (command.getCommand()){
            case j:
                nextInstrcutionIdx = gotoLabel(add);
                break;
            case jal:
                // TODO: see the difference between them
                nextInstrcutionIdx = gotoLabel(add);
                break;
            default:
                return false;
        }
        return true;
    }
    private int gotoLabel(int label){
        //@TODO: make it takes label as input and returns label index
        return 0;
    }
    // Takes index not offset
    private int getFromMemory(int memoryIndex){
        return memory[memoryIndex*4 + MEMORY_START];
    }
    // Takes index not offset
    private boolean setToMemory(int data, int memoryIndex){
        memory[memoryIndex*4 + MEMORY_START] = data;
        return true;
    }
    private int getFromRegisters(int registerIndex){
        return registers[registerIndex];
    }
    private boolean setToRegister(int data, int registerIndex){
        registers[registerIndex] = data;
        return true;
    }

}
