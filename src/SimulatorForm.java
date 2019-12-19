import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
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
    private JTextField pcText;
    private JTable textTable;
    private JTextField loText;
    private JTextField hiText;

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
        DefaultTableModel model = new DefaultTableModel() {
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
            String number = Integer.toString(i << 2, 16);
            model.addRow(new Object[]{"0x" + ("00000000" + number).substring(number.length()), mem.getValue(i)});
        }
    }

    private void buildRegisters() {
        DefaultTableModel model = new DefaultTableModel() {
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

    private void setPC(int value) {
        pcText.setText(String.valueOf(value));
    }

    private int getPC() {
        int pc = Integer.parseInt(pcText.getText(), 16);
        return pc;
    }

    private void updateCurrentInstruction() {
        Instruction currentInstruction = null;
        try {
            currentInstruction = simulator.fetchNextInstruction();
            currebtInstructionTxt.setText(currentInstruction.toAssembly());
        } catch (Exception e) {
            currebtInstructionTxt.setText("Terminated");
        }
    }

    SimulatorForm(Simulator s) {
        simulator = s;

        buildMemory();
        buildRegisters();
        buildText();
        setPC(s.getPC());

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

        pcText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    simulator.setPC(getPC());
                    updateCurrentInstruction();
                } catch (Exception ex) {
                    addException(ex);
                }
            }
        });
        loText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                simulator.setLoRegister(Integer.parseInt(loText.getText()));
            }
        });
        hiText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                simulator.setLoRegister(Integer.parseInt(hiText.getText()));
            }
        });

        registerTable.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!registerTable.isEditing()) {
                    int row = registerTable.getSelectedRow();
                    int col = registerTable.getSelectedColumn();
                    int value = simulator.getRegisters().getValue(row);
                    try {
                        value = Integer.parseInt(registerTable.getValueAt(row, col).toString());
                    } catch (Exception ignored) {
                    }
                    simulator.getRegisters().setValue(row, value);
                }
            }
        });
        memoryTable.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!memoryTable.isEditing()) {
                    int row = memoryTable.getSelectedRow();
                    int col = memoryTable.getSelectedColumn();
                    int value = simulator.getMemory().getValue(row);
                    try {
                        value = Integer.parseInt(memoryTable.getValueAt(row, col).toString());
                    } catch (Exception ignored) {
                    }
                    simulator.getMemory().setValue(row, value);
                }
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
        setPC(simulator.getPC());
    }

    private void runSingleInstruction() {
        try {
            UpdateHandler uh = new UpdateHandler();
            simulator.executeNextInstruction(uh);
            pcText.setText(String.valueOf(simulator.getPC()));

            DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
            for (int v : uh.getScheduledMemory()) {
                model.setValueAt(simulator.getMemory().getValue(v), v, 1);
            }
            model = (DefaultTableModel) registerTable.getModel();
            for (int v : uh.getScheduledRegisters()) {
                model.setValueAt(simulator.getRegisters().getValue(v), v, 1);
            }
            if (uh.getScheduledLoHi()) {
                loText.setText(String.valueOf(simulator.getLoRegister()));
                hiText.setText(String.valueOf(simulator.getHiRegister()));
            }
            setPC(simulator.getPC());
            updateCurrentInstruction();
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
                updateCurrentInstruction();
            } catch (Exception e) {
                addException(e);
            }
            pcText.setEditable(true);
            runEntireButton.setEnabled(true);
            runInstructionButton.setEnabled(true);
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
