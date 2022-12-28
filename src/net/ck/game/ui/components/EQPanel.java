package net.ck.game.ui.components;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.items.ArmorPositions;
import net.ck.game.ui.dnd.*;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;

public class EQPanel extends JPanel implements EQPanelArmorType
{

    private ArmorPositions armorPosition;

    public void paintComponent(Graphics g)
    {
        g.setColor(Color.black);
        g.drawOval(0,0, GameConfiguration.elipseSize, GameConfiguration.elipseSize);
        if (Game.getCurrent().getCurrentPlayer().getWearEquipment().get(getArmorPosition()) != null)
        {
            g.drawImage(Game.getCurrent().getCurrentPlayer().getWearEquipment().get(getArmorPosition()).getItemImage(), 1, 1, null);
        }

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

    public EQPanel()
    {
        //this.setDragEnabled(true);
        //this.setDropMode(DropMode.ON);
        this.setTransferHandler(new EQPanelTransferHandler(this));
        DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new EQPanelDragGestureHandler(this));
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new EQPanelDropTargetHandler(this), true);
    }
}
