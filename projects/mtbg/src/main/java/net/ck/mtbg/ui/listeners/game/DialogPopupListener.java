package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.game.InventoryPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Log4j2
@Getter
@Setter
public class DialogPopupListener extends MouseAdapter
{
    private InventoryPane component;

    public DialogPopupListener(InventoryPane inventoryPane)
    {
        setComponent(inventoryPane);
    }

    public void mousePressed(MouseEvent e)
    {
        logger.info("pressed");
        maybeShowPopup(e);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        logger.info("clicked");
    }

    public void mouseReleased(MouseEvent e)
    {
        logger.info("released");
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            getComponent().getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
        }
        else
        {
            logger.info(getComponent().getParent().toString());
        }
    }
}
