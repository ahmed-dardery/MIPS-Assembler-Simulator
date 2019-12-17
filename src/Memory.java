import java.util.List;

public class Memory {
    private int[] memory;
    private static final int BYTE_MASK = 0xFF, HALF_WORD_MASK = 0xFFFF;

    Memory(int memorySizeInWords) {
        memory = new int[memorySizeInWords];
    }
    public void fillMemoryWithInstructions(List<Instruction> instructions){
        int offset = 0;
        for (Instruction instruction : instructions)
            memory[offset++] = instruction.toMachineLanguage();
    }
    //Memory is stored as a word (4 bytes), this reads and sets a particular byte.
    public byte getByte(int memoryAddress) {
        int pos = (memoryAddress & 0b11) * 8;
        return (byte) ((memory[memoryAddress >> 2] >> pos) & BYTE_MASK);
    }

    public void setByte(int memoryAddress, byte data) {
        int pos = (memoryAddress & 0b11) * 8;
        memory[memoryAddress >> 2] = memory[memoryAddress >> 2] & ~(BYTE_MASK << pos) | (int) data << pos;
    }

    public short getHalfWord(int memoryAddress) throws Exception {
        if ((memoryAddress & 0b1) != 0) throw new Exception("not half word aligned.");
        memoryAddress>>=1;

        int pos = (memoryAddress & 0b1) * 16;
        return (short) ((memory[memoryAddress >> 2] >> pos) & HALF_WORD_MASK);
    }

    public void setHalfWord(int memoryAddress, short data) throws Exception {
        if ((memoryAddress & 0b1) != 0) throw new Exception("not half word aligned.");
        memoryAddress>>=1;

        int pos = (memoryAddress & 0b1) * 16;
        memory[memoryAddress >> 2] = memory[memoryAddress >> 2] & ~(HALF_WORD_MASK << pos) | (int) data << pos;
    }

    public int getWord(int memoryAddress) throws Exception {
        if ((memoryAddress & 0x11) != 0) throw new Exception("not word aligned.");
        return memory[memoryAddress >> 2];
    }

    public void setWord(int memoryAddress, int data) throws Exception {
        if ((memoryAddress & 0x11) != 0) throw new Exception("not word aligned.");
        memory[memoryAddress >> 2] = data;
    }

    public int getValue(int index){
        return memory[index];
    }

    public void setValue(int index, int data){
        memory[index] = data;
    }
}
