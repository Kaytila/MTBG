package net.ck.game.demos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
 
public class DropDemo extends JPanel implements ActionListener {
    private JComboBox dropCombo;
    private JList list;
     
    public DropDemo() {
        super(new GridLayout(2,1));
        add(createArea());
        add(createList());
    }
     
    private JPanel createList() {
        DefaultListModel listModel = new DefaultListModel();
             
        for (int i = 0; i < 10; i++) {
            listModel.addElement("List Item " + i);
        }
         
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400,100));
         
        list.setDragEnabled(true);
        list.setTransferHandler(new ListTransferHandler());
         
        dropCombo = new JComboBox(new String[] {"USE_SELECTION", "ON", "INSERT", "ON_OR_INSERT"});
        dropCombo.addActionListener(this);
        JPanel dropPanel = new JPanel();
        dropPanel.add(new JLabel("List Drop Mode:"));
        dropPanel.add(dropCombo);
         
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(dropPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createTitledBorder("List"));
        return panel;
    }
     
    private JPanel createArea() {
        String text = "Drag from or drop into this area.\nThe default action is MOVE;\nhold the Control key to COPY.";
         
        JTextArea area = new JTextArea();
        area.setText(text);
        area.setDragEnabled(true);
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(400,100));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Text Area"));
        return panel;
    }
 
    public void actionPerformed(ActionEvent ae) {
        Object val = dropCombo.getSelectedItem();
        if (val == "USE_SELECTION") {
            list.setDropMode(DropMode.USE_SELECTION);
        } else if (val == "ON") {
            list.setDropMode(DropMode.ON);
        } else if (val == "INSERT") {
            list.setDropMode(DropMode.INSERT);
        } else if (val == "ON_OR_INSERT") {
            list.setDropMode(DropMode.ON_OR_INSERT);
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("DropDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new DropDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}