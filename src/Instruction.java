//TODO: figure out what to do with jump instructions (because they jump to a label)
public interface Instruction {
    /**
     * @return short instruction name in assembly
     */
    String getInstructionName();
    /**
     * @return id of instruction, it is opcode for non-R type and concatenation of opcode and funct for R-type
     */
    int getIdentifier();
    /**
     * @return op code of instruction, 0 for R-type
     */
    int getOpCode();
    /**
     * @return Assembly String for the instruction, for example "add $s0, $s1, $s2".
     */
    String toAssembly();
    /**
     * @return Machine Language for the instruction in binary formation.
     */
    String toMachineLanguage();
}
