import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Simulator s = null;
        try {
            List<String> example = Files.readAllLines(Paths.get("Test.in"));
            s = new Simulator(Parser.parseLines(example));
            s.executeAllInstructions();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(s.getRegisters().getValue(RegisterNames.getRegisterIndex("$t1")));
        }

    }
}
