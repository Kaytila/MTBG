package net.ck.game.ui;

import javax.swing.*;
import java.awt.*;

public class EQPanel extends JComponent
{
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.black);
        g.drawOval(0,0, 50, 50);
    }
}
