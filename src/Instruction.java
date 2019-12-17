import java.util.Map;

public abstract class Instruction {
    private static final boolean BRANCH_DELAY_SLOT = true;

    public enum InstructionType {
        RTypeInstruction,
        JTypeInstruction,
        ITypeInstruction
    }
    public Instruction(){}

    public static Instruction parseString(String input, int idx, Map<String, Integer> labelResolver) {
        input = input.trim().replace("(", ",").replace(")", "").replaceAll("\\s+,\\s+|\\s+,|,\\s+", ",");

        String[] result = input.split("\\s+|,");

        String cmd = result[0];

        if (jType(cmd)) idx = 0;
        else if (BRANCH_DELAY_SLOT) idx++;

        int[] args = parseArguments(result, idx, labelResolver);


        Instruction ret;
        if (jType(cmd)) {
            ret = new JTypeInstruction(JTypeInstruction.JTypeNames.valueOf(cmd), args[1]);
        }
        else if (iType(cmd,result)) {
            ret = new ITypeInstruction(ITypeInstruction.ITypeNames.valueOf(cmd), args[1], args[2], args[3]);
        }
        else if (rType(cmd,result)) {
            ret = new RTypeInstruction(RTypeInstruction.RTypeNames.valueOf(cmd), args[1], args[2], args[3]);
        }
        else {
            System.out.println("bad input");
            return null;
        }
        return ret;
    }


    private static int[] parseArguments(String[] args, int idx, Map<String, Integer> labelResolver) {
        int[] res = new int[4];
        for (int i = 1; i < Math.min(args.length, 4); ++i) {
            if (!args[i].startsWith("$")) {
                Integer ret = labelResolver.get(args[i]);
                if (ret == null)
                    res[i] = Integer.parseInt(args[i]);
                else
                    res[i] = ret - idx;
            } else {
                res[i] = RegisterNames.getRegisterIndex(args[i]);
            }
        }
        return res;
    }

    private static boolean rType(String input,String [] result) {
        try {
            RTypeInstruction.RTypeNames.valueOf(input);
            RTypeInstruction.Reg decodeOrder = RTypeInstruction.RTypeNames.getReg(input);
            if(checkArgs(result,decodeOrder.toString()))
                return true;
        }
        catch (Exception ignored) {
            return false;
        }
        return false;
    }

    public static boolean iType(String input,String [] result) {
        try {
            ITypeInstruction.ITypeNames.valueOf(input);
            ITypeInstruction.Reg decodeOrder = ITypeInstruction.ITypeNames.getReg(input);
            if(checkArgs(result,decodeOrder.toString()))
                return true;
        }
        catch (Exception ignored) {
            return false;
        }
        return false;
    }

    private static boolean jType(String input) {
        try {
            JTypeInstruction.JTypeNames.valueOf(input);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean checkArgs(String[] result,String decodeOrder) {
        System.out.println(decodeOrder);
        switch (decodeOrder) {
            case "RT_RS_IMM":
            case "RD_RT_SHAMT":
                if (result.length!=4 || !result[1].startsWith("$") || !result[2].startsWith("$") || !result[3].matches("\\d+"))
                    return false;
            break;

            case "RD_RS_RT":
            case "RD_RT_RS":
                if(result.length!=4 || !result[1].startsWith("$") || !result[2].startsWith("$") || !result[3].startsWith("$"))
                    return false;
                break;

            case"RT_IMM_RS":
                if(result.length!=4 || !result[1].startsWith("$") || !result[2].matches("\\d+") || !result[3].startsWith("$"))
                    return false;
                break;

            case"RT_IMM":
                if(result.length!=3 || !result[1].startsWith("$") || !result[2].matches("\\d+"))
                    return false;
                break;

            case"RS_RT":
                if(result.length!=3 || !result[1].startsWith("$") || !result[2].startsWith("$"))
                    return false;
                break;

            case"RS":
            case"RD":
                if(result.length!=2 || !result[1].startsWith("$"))
                    return false;
                break;

                default:
                    return false;
        }
        return true;
    }

    /**
     * @return the instruction type of the class
     */
    public abstract InstructionType getInstructionType();

    /**
     * @return short instruction name in assembly
     */
    public abstract String getInstructionName();

    /**
     * @return id of instruction, it is opcode for non-R type and concatenation of opcode and funct for R-type
     */
    public abstract int getIdentifier();

    /**
     * @return op code of instruction, 0 for R-type
     */
    public abstract int getOpCode();

    /**
     * @return Assembly String for the instruction, for example "add $s0, $s1, $s2".
     */
    public abstract String toAssembly();

    /**
     * @return Machine Language for the instruction in binary formation.
     */
    public abstract int toMachineLanguage();

    int adjustBits(int value, int bits, int shift) {
        return (value & ((1 << bits) - 1)) << shift;
    }


}
