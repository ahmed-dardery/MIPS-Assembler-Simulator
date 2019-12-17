import java.util.LinkedList;
import java.util.List;

public class UpdateHandler {
    private List<Integer> mem = new LinkedList<>();
    private List<Integer> reg = new LinkedList<>();

    private boolean lohi = false;

    public void scheduleMemory(int idx) {
        mem.add(idx);
    }

    public List<Integer> getScheduledMemory() {
        return mem;
    }

    public void scheduleRegister(int idx) {
        reg.add(idx);
    }

    public List<Integer> getScheduledRegisters() {
        return reg;
    }

    public void scheduleLoHi() {
        lohi = true;
    }

    public boolean getScheduledLoHi() {
        return lohi;
    }
}
