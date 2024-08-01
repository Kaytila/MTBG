package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.EQPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

@Getter
@Setter
@Log4j2
@ToString
public class EQPanelTransferHandler extends TransferHandler
{


    public EQPanelTransferHandler(EQPanel headPanel)
    {
    }

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


}
