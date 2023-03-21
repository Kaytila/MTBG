package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.items.ArmorPositions;
import net.ck.mtbg.ui.dnd.EQPanelDragGestureHandler;
import net.ck.mtbg.ui.dnd.EQPanelDropTargetHandler;
import net.ck.mtbg.ui.dnd.EQPanelTransferHandler;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;

public class EQPanel extends JComponent implements EQPanelArmorType
{

    private       ArmorPositions armorPosition;
    private final Logger         logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public void paintComponent(Graphics g)
    {
        logger.info("painting eq panel");
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
