import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SimulatorForm extends JFrame {
    private JPanel panel;
    private JTable registerTable;
    private JTable memoryTable;
    private JButton runInstructionButton;
    private JButton runEntireButton;
    private JButton clearRegistersButton;
    private JButton clearMemoryButton;
    private JButton uploadCodeButton;
    private JTextPane codePane;
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

    private static final int MEMORY_SIZE = (1 << 15), REGISTER_SIZE = (1 << 5);
    private String assemblyCode;
    private int[] memory, registers;


    private void buildMemory() {
        DefaultTableModel memoryModel = (DefaultTableModel) memoryTable.getModel();

        memoryModel.setColumnCount(0);
        memoryModel.addColumn("Address");
        memoryModel.addColumn("Value");

        memoryModel.setRowCount(0);
        for(int i = 0 ; i < MEMORY_SIZE ; ++i){
            //TODO edit the address(i) that added to the table row
            memoryModel.addRow(new Object[]{i, memory[i]});
        }
    }

    private void buildRegister() {
        DefaultTableModel registerModel = (DefaultTableModel) registerTable.getModel();

        registerModel.setColumnCount(0);
        registerModel.addColumn("Address");
        registerModel.addColumn("Value");

        registerModel.setRowCount(0);
        for(int i = 0 ; i < REGISTER_SIZE ; ++i){
            //TODO edit the address(i) that added to the table row
            registerModel.addRow(new Object[]{i, registers[i]});
        }
    }

    private void readAndSetCode(File assemblyCodeFile) throws FileNotFoundException {
        Scanner s = new Scanner(assemblyCodeFile);

        while (s.hasNextLine())
            assemblyCode += s.nextLine() + '\n';

        codePane.setText(assemblyCode);
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
                clearMemory();
            }
        });
        clearRegistersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearRegisters();
            }
        });
        uploadCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File assemblyCodeFile;
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
                jfc.addChoosableFileFilter(filter);

                try {
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        assemblyCodeFile = jfc.getSelectedFile();
                        readAndSetCode(assemblyCodeFile);
                    } else
                        throw new Exception("Can not upload the Selected File");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //TODO Update Program Counter
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

    public void clearMemory(){
        for(int i = 0 ; i < MEMORY_SIZE ; ++i){
            //TODO Add Getter and Setter if you want.
            memory[i] = 0;
        }
        buildMemory();
    }

    public void clearRegisters(){
        for(int i = 0 ; i < REGISTER_SIZE ; ++i){
            //TODO Add Getter and Setter if you want.
            registers[i] = 0;
        }
        buildRegister();
    }

    public static void main(String[] args) {
        int[] memory = new int[(1 << 15)];
        int[] register= new int[(1 << 5)];
        SimulatorForm sf = new SimulatorForm(memory, register);
    }
}
