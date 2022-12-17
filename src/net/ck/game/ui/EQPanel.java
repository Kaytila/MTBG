package net.ck.game.ui;

import net.ck.game.items.ArmorPositions;

import javax.swing.*;
import java.awt.*;

public class EQPanel extends JComponent implements EQPanelArmorType
{

    private ArmorPositions armorPosition;

    public void paintComponent(Graphics g)
    {
        g.setColor(Color.black);
        g.drawOval(0,0, 50, 50);
    }

    @Override
    public ArmorPositions getArmorPosition()
    {
        return armorPosition;
    }

    @Override
    public void setArmorPosition(ArmorPositions position)
    {
        armorPosition = position;
    }
}
