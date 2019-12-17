import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class SimulatorForm extends JFrame {
    private JPanel panel;
    private JTable registerTable;
    private JTable memoryTable;
    private JButton runInstructionButton;
    private JButton runEntireButton;
    private JButton clearRegistersButton;
    private JButton clearMemoryButton;
    private JButton uploadCodeButton;
    private JTextPane instructionsPane;
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

    private File CodeFile;
    private String assemblyCode;
    private int[] memory, registers;


    private void buildMemory() {
        DefaultTableModel memoryModel = (DefaultTableModel) memoryTable.getModel();
        memoryModel.addColumn("Address");
        memoryModel.addColumn("Value");
    }

    private void buildRegister() {
        DefaultTableModel memoryModel = (DefaultTableModel) registerTable.getModel();
        memoryModel.addColumn("Address");
        memoryModel.addColumn("Value");
    }

    SimulatorForm(int[] memory, int[] registers) {
        this.memory = memory;
        this.registers = registers;

        buildMemory();
        buildRegister();

        add(panel);
        setTitle("Assembler");
        setSize(1100, 700);
        setVisible(true);

        clearMemoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Clear Memory
            }
        });
        clearRegistersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Clear Register
            }
        });
        uploadCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Upload File
            }
        });
        runInstructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Run Instruction by instruction
            }
        });
        runEntireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Run Entire Program
            }
        });
        RefineryUtilities.centerFrameOnScreen(this);
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
    }

    public void addException(Exception e) {
        String exception = exceptionPane.getText();
        exception += e.toString() + '\n';
        exceptionPane.setText(exception);
    }

    public void setCurrentInstruction(Instruction instruction) {
        currebtInstructionTxt.setText(instruction.toAssembly());
    }

    public void setInstructionDescription(String des) {
        instructionDecPane.setText(des);
    }

    public static void main(String[] args) {
        SimulatorForm sf = new SimulatorForm(null, null);
    }
}
