package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.ui.components.game.InventoryPane;
import net.ck.mtbg.ui.renderers.game.InventoryPaneListCellRenderer;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;

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
     * <a href="https://stackoverflow.com/questions/8976874/show-two-dialogs-on-top-of-each-other-using-java-swing">
     * https://stackoverflow.com/questions/8976874/show-two-dialogs-on-top-of-each-other-using-java-swing</a>
     *
     * @param owner     - the owning JFrame
     * @param title     - the title string for the dialog
     * @param modal     - modal or not
     * @param inventory - whats the inventory in question
     */
    public ContainerDialog(JFrame owner, String title, boolean modal, Inventory inventory)
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
