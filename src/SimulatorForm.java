import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimulatorForm extends JFrame {
    private JPanel panel;
    private JTable registerTable;
    private JTable memoryTable;
    private JButton runInstructionButton;
    private JButton runEntireButton;
    private JButton clearRegistersButton;
    private JButton clearMemoryButton;
    private JButton uploadCodeButton;
    private JScrollPane instructionScrollPanel;
    private JScrollPane memoryScrollPanel;
    private JScrollPane registerScroll;
    private JPanel instructionPanel;
    private JPanel registerPanel;
    private JPanel memoryPanel;
    private JTextPane exceptionPane;
    private JPanel runPanel;
    private JTextPane instructionDecPane;
    private JTextField currebtInstructionTxt;
    private JTextField textField1;
    private JTable textTable;

    private String assemblyCode;

    final Simulator simulator;

    private void buildText() {
        DefaultTableModel model = (DefaultTableModel) textTable.getModel();
        model.setColumnCount(0);
        model.addColumn("Address");
        model.addColumn("Assembly");
        model.addColumn("Content");
    }

    private void resyncText() {
        DefaultTableModel model = (DefaultTableModel) textTable.getModel();
        model.setRowCount(0);
        List<Instruction> lst = simulator.getInstructionList();
        for (int i = 0; i < lst.size(); i++) {
            model.addRow(new Object[]{"0x" + Integer.toString(i << 2, 16), lst.get(i).toAssembly(), lst.get(i).toMachineLanguage()});
        }
    }

    private void buildMemory() {
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
//        DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
        model.setColumnCount(0);
        model.addColumn("Address");
        model.addColumn("Value");
        memoryTable.setModel(model);
    }

    private void resyncMemory() {
        DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
        model.setRowCount(0);
        Memory mem = simulator.getMemory();
        for (int i = 0; i < mem.getMemorySize(); i++) {
            model.addRow(new Object[]{"0x" + Integer.toString(i << 2, 16), mem.getValue(i)});
        }
    }

    private void buildRegisters() {
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
//        DefaultTableModel model = (DefaultTableModel) registerTable.getModel();
        model.setColumnCount(0);
        model.addColumn("Register");
        model.addColumn("Content");
        registerTable.setModel(model);
    }

    private void resyncRegisters() {
        DefaultTableModel model = (DefaultTableModel) registerTable.getModel();
        model.setRowCount(0);
        Memory mem = simulator.getRegisters();
        for (int i = 0; i < mem.getMemorySize(); i++) {
            model.addRow(new Object[]{RegisterNames.getRegisterIdentifier(i), mem.getValue(i)});
        }
    }

    SimulatorForm(Simulator s) {
        simulator = s;

        buildMemory();
        buildRegisters();
        buildText();

        clearMemory();
        clearRegisters();

        add(panel);
        setTitle("Assembler");
        setSize(1100, 700);
        setVisible(true);

        clearMemoryButton.addActionListener(e -> clearMemory());
        clearRegistersButton.addActionListener(e -> clearRegisters());
        uploadCodeButton.addActionListener(e -> uploadCode());
        runInstructionButton.addActionListener(e -> runSingleInstruction());
        runEntireButton.addActionListener(e -> runAllInstructions());
        memoryTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyTyped(e);
                int row = memoryTable.getSelectedRow();
                int col = memoryTable.getSelectedColumn();
                String value = memoryTable.getValueAt(row, col).toString();
                //TODO USE Row, Col to update the memory
            }
        });
        registerTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyTyped(e);
                int row = registerTable.getSelectedRow();
                int col = registerTable.getSelectedColumn();
                String value = registerTable.getValueAt(row, col).toString();
                //TODO USE Row, Col to update the registers
            }
        });
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //TODO Update Program Counter
            }
        });
    }

    private void runAllInstructions() {
        try {
            simulator.executeAllInstructions();
        } catch (Exception ex) {
            addException(ex);
        }
        resyncMemory();
        resyncRegisters();
        textField1.setText(String.valueOf(simulator.getPC()));

    }

    private void runSingleInstruction() {
        try {
            UpdateHandler uh = new UpdateHandler();
            simulator.executeNextInstruction(uh);
            textField1.setText(String.valueOf(simulator.getPC()));

            DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
            for (int v : uh.getScheduledMemory()) {
                model.setValueAt(simulator.getMemory().getValue(v), v, 1);
            }
            model = (DefaultTableModel) registerTable.getModel();
            for (int v : uh.getScheduledRegisters()) {
                model.setValueAt(simulator.getRegisters().getValue(v), v, 1);
            }
            if (uh.getScheduledLoHi()){
                //TODO: update LO and HI textboxes.
            }

        } catch (Exception ex) {
            addException(ex);
        }
    }

    private void uploadCode() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File assemblyCodeFile = jfc.getSelectedFile();
            try {
                simulator.setInstructionList(Parser.parseFile(assemblyCodeFile));
                resyncText();
            } catch (IOException e) {
                addException(e);
            }
        }
    }


    public void addException(Exception e) {
        String exception = exceptionPane.getText();
        exception += e.toString() + '\n';
        e.printStackTrace();
        exceptionPane.setText(exception);
    }

    public void clearMemory() {
        for (int i = 0; i < simulator.getMemory().getMemorySize(); ++i) {
            simulator.getMemory().setValue(i, 0);
        }
        resyncMemory();
    }

    public void clearRegisters() {
        for (int i = 0; i < simulator.getRegisters().getMemorySize(); ++i) {
            simulator.getRegisters().setValue(i, 0);
        }
        resyncRegisters();
    }
}
