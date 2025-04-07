package net.ck.mtbg.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

@Log4j2
@Getter
@Setter
public class VerfluchtesDing
{
    public VerfluchtesDing()
    {
        //Create and set up the window.
        JFrame frame = new JFrame("ScrollDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        LabelEntryField2 labelEntryField = new LabelEntryField2("Mapsize:", "10@10", new Point(10, 10));
        labelEntryField.setBounds(10, 10, 100, 100);
        frame.add(labelEntryField);
        frame.setVisible(true);
    }

    private static void createAndShowGUI()
    {
        VerfluchtesDing ding = new VerfluchtesDing();

    }

    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    class LabelEntryField2 extends JPanel
    {
        final int spacing = 5;
        JLabel label;
        JTextField entryfield;

        Point startingPoint;


        public LabelEntryField2(String labelString, String value, Point startingPoint)
        {
            label = new JLabel(labelString);
            entryfield = new JTextField(value);

            label.setBounds(startingPoint.x, startingPoint.y, 5, 5);
            entryfield.setBounds(startingPoint.x + spacing, startingPoint.y, 5, 5);
            this.setBounds(startingPoint.x, startingPoint.y, 5 + 5, 5 + 5);
            this.add(entryfield);
            this.add(label);

            label.setVisible(true);
            entryfield.setVisible(true);
            this.setVisible(true);
            this.setBackground(Color.cyan);

        }

        protected void paintComponent(Graphics g)
        {
            if (startingPoint == null)
            {
                return;
            }
            super.paintComponent(g);

            //TODO how to do this properly?
            Graphics2D g2d = (Graphics2D) g;
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(label.getText(), g2d);
            int x = (this.getWidth() - (int) r.getWidth()) / 2;
            int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
            label.setBounds(startingPoint.x, startingPoint.y, x, y);


            //calculate entry field use default if null
            if (entryfield.getText().isEmpty())
            {
                entryfield.setText("12345");
            }
            Rectangle2D r1 = fm.getStringBounds(entryfield.getText(), g2d);
            int x1 = (this.getWidth() - (int) r1.getWidth()) / 2;
            int y1 = (this.getHeight() - (int) r1.getHeight()) / 2 + fm.getAscent();
            entryfield.setBounds(startingPoint.x + x + spacing, startingPoint.y, x1, y1);
            this.setBounds(startingPoint.x, startingPoint.y, x + x1, y + y1);
        }
    }

}
