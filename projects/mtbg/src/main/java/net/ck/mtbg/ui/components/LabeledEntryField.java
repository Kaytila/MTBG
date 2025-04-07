package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class LabeledEntryField extends JComponent
{
    private final JLabel labelHolder = new JLabel();
    private final JTextField valueHolder = new JTextField();
    Point startingPoint;


    public LabeledEntryField(String labelString, String valueString)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        labelHolder.setText(labelString);
        valueHolder.setText(valueString);
        add(labelHolder);
        add(valueHolder);

    }

    private static String _toLabelString(final String label)
    {
        if (label == null)
        {
            return null;
        }
        final var cleaned = label.trim();
        if (cleaned.endsWith(":"))
        {
            return cleaned;
        }
        return cleaned + ":";
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /*if (startingPoint == null)
        {
            return;
        }*/


        //TODO how to do this properly?
        /*Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(labelHolder.getText(), g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        labelHolder.setBounds(startingPoint.x, startingPoint.y, x, y);


        //calculate entry field use default if null
        if (valueHolder.getText().isEmpty())
        {
            valueHolder.setText("12345");
        }
        Rectangle2D r1 = fm.getStringBounds(valueHolder.getText(), g2d);
        int x1 = (this.getWidth() - (int) r1.getWidth()) / 2;
        int y1 = (this.getHeight() - (int) r1.getHeight()) / 2 + fm.getAscent();
        valueHolder.setBounds(startingPoint.x + x + spacing, startingPoint.y, x1, y1);
        this.setBounds(startingPoint.x, startingPoint.y, x + x1 + spacing, y + y1);
        */
    }

    public String getLabel()
    {
        return labelHolder.getText();
    }

    public void setLabel(
            final String label)
    {
        labelHolder.setText(_toLabelString(label));
    }

    public String getValue()
    {
        return valueHolder.getText();
    }

    public void setValue(
            final String value)
    {
        valueHolder.setText(value);
    }

}
