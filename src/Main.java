import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SimulatorForm f = new SimulatorForm(new Simulator());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
