import java.util.*;

public class Parser {
    public static List<Instruction> parseLines(Iterable<String> lines) {
        Map<String, Integer> labelResolver = new HashMap<>();

        List<String> commands = new LinkedList<>();

        int idx = 0;
        for (String in : lines) {
            in = in.trim();
            if (in.isEmpty()) continue;
            if (in.charAt(in.length() - 1) == ':') {
                labelResolver.put(in.substring(0, in.length() - 1), idx);
            } else {
                commands.add(in);
                idx++;
            }
        }

        List<Instruction> ret = new ArrayList<>();
        idx = 0;
        for (String instr : commands) {
            ret.add(Instruction.parseString(instr, idx, labelResolver));
            idx++;
        }
        return ret;
    }
    public static List<Instruction> parseString(String input) {
        return parseLines(Arrays.asList(input.split("\n")));
    }
}
