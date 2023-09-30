package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.ui.components.Spellbook;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.CursorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * spellbooklistener is the general listener class for SpellBook Table Component.
 * Currently, only mouse listener, but will definitely include more in the future.
 *
 * <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple">https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple</a>
 */
public class SpellBookListener implements MouseListener, MouseMotionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    @Override
    public void mouseClicked(MouseEvent e)
    {
        logger.debug("simple mouse click");
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Spellbook table = (Spellbook) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        int column = table.columnAtPoint(point);


        if (e.getClickCount() == 2 && table.getSelectedRow() != -1)
        {
            logger.info("row: {}, column: {}", row, column);
            table.getCellEditor().stopCellEditing();
        }
        else if (e.getClickCount() == 1 && table.getSelectedRow() != -1)
        {
            logger.info("row: {}, column: {} is selected", row, column);
            table.setSelectionPoint(new Point(row, column));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        logger.debug("simple mouse released");
        Spellbook table = (Spellbook) e.getSource();
        table.changeSelection(table.getSelectionPoint().x, table.getSelectionPoint().y, false, false);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        logger.debug("simple mouse entered");
        Spellbook table = (Spellbook) e.getSource();
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CursorUtils.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        logger.debug("simple mouse exit");
        Spellbook table = (Spellbook) e.getSource();
        table.setCursor(Cursor.getDefaultCursor());
        CursorUtils.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        return;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        logger.info("hmmm");
    }
}
