

import java.util.List;

public class Simulator {
    private List<Instruction> instructions;
    private int nextInstrcutionIdx; // Acts like program counter but uses index in the list | -1 -> finish
    private final int memoryOffset; // start of the program in the memory
    static final int MEMORY_SIZE = (1<<15),REGISTER_SIZE = (1<<5);
    private int[] memory = new int[MEMORY_SIZE] , registers = new int [REGISTER_SIZE];

    Simulator(List<Instruction> instructions,int memoryOffset){
        this.instructions = instructions;
        this.memoryOffset = memoryOffset;

        fillMemoryWithInstructions();
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
        return true;
    }
    private boolean JInstructionExecute(Instruction current){
        // TODO: execute all J type instructions here given an instruction of J type as a parameter and return false if you faced any error
        // TODO: Also update here the program counter
        // TODO : Adham
        return true;
    }
    private boolean executeLabel(int label){
        //@TODO: make it takes label as input and modify in the program counter to the label
        return true;
    }
    // Takes index not offset
    private int getFromMemory(int memoryIndex){
        return memory[memoryIndex];
    }
    // Takes index not offset
    private boolean setToMemory(int data, int memoryIndex){
        memory[memoryIndex] = data;
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
