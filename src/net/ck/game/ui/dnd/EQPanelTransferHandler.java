package net.ck.game.ui.dnd;

import net.ck.game.ui.components.EQPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.util.Objects;

public class EQPanelTransferHandler extends TransferHandler
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    @Override
    public void setDragImage(Image img)
    {
        super.setDragImage(img);
    }

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action)
    {
        super.exportAsDrag(comp, e, action);
    }

    @Override
    public boolean importData(TransferSupport support)
    {
        return super.importData(support);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t)
    {
        return super.importData(comp, t);
    }

    @Override
    public boolean canImport(TransferSupport support)
    {
        return super.canImport(support);
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
    {
        return super.canImport(comp, transferFlavors);
    }

    @Override
    public int getSourceActions(JComponent c)
    {
        return super.getSourceActions(c);
    }

    @Override
    protected Transferable createTransferable(JComponent c)
    {
        return super.createTransferable(c);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action)
    {
        super.exportDone(source, data, action);
    }

    public EQPanelTransferHandler(EQPanel headPanel)
    {
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
