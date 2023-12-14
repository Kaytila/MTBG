package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.ui.components.InventoryPane;
import net.ck.mtbg.ui.renderers.InventoryPaneListCellRenderer;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class ContainerDialog extends AbstractDialog
{


    public ContainerDialog()
    {

    }

    /**
     * Might be that I can reuse the full inventory dialog again
     * https://stackoverflow.com/questions/8976874/show-two-dialogs-on-top-of-each-other-using-java-swing
     *
     * @param owner
     * @param title
     * @param modal
     * @param inventory
     */
    public ContainerDialog(Frame owner, String title, boolean modal, Inventory inventory)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        this.setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 300);
        panel.setLayout(null);
        this.setContentPane(panel);

        InventoryPane invP = new InventoryPane(owner, inventory, this, null);
        invP.setBounds(0, 0, 300, 200);
        this.add(invP.initializeScrollPane());

        InventoryPaneListCellRenderer listCellRenderer = new InventoryPaneListCellRenderer();
        invP.setCellRenderer(listCellRenderer);

        addButtons();
        this.setVisible(true);
    }
}
