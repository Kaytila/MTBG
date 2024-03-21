package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.ui.dialogs.AbstractDialog;
import net.ck.mtbg.ui.dnd.InventoryPaneTransferHandler;
import net.ck.mtbg.ui.listeners.DialogPopupListener;
import net.ck.mtbg.ui.listeners.InventoryPaneListener;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class InventoryPane extends JList<AbstractItem>
{
    private JScrollPane sp;
    private JPopupMenu menu;
    private ListSelectionModel listSelectionModel;
    private int selectionIndex;

    private InventoryPaneListener generalListener;
    private Frame owner;
    private AbstractDialog parentDialog;

    public InventoryPane(Frame owner, Inventory inventory, AbstractDialog dialog, AbstractKeyboardAction action)
    {
        super();
        this.setOwner(owner);
        this.setParentDialog(dialog);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setVisible(true);
        this.setFont(GameConfiguration.font);

        this.setAutoscrolls(true);
        this.setBorder(blackline);
        //this.setToolTipText(getLogger().getName());
        this.requestFocusInWindow();
        // one way of doing drag and drop
        this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);
        this.setTransferHandler(new InventoryPaneTransferHandler());
		 
		/* this is another way to do drag and drop
		DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new InventoryPaneDragGestureHandler(this));

		DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new InventoryPaneDropTargetHandler(this), true);
		*/
        setGeneralListener(new InventoryPaneListener(this, action));
        listSelectionModel = this.getSelectionModel();
        listSelectionModel.addListSelectionListener(getGeneralListener());
        setModel(inventory);
        getModel().addListDataListener(getGeneralListener());
        this.addMouseListener(getGeneralListener());
				/*for (AbstractItem i : Game.getCurrent().getCurrentPlayer().getInventory().getInventory())
		{
			/logger.info("item: {}", i.toString());
		}*/
        addPopUpMenu();
    }

    public InventoryPane()
    {

    }

    public JScrollPane initializeScrollPane()
    {
        sp = new JScrollPane(this);
        sp.setBounds(0, 0, 250, 200);
        sp.setVisible(true);
        return sp;
    }


    private void addPopUpMenu()
    {
        menu = new JPopupMenu();
        //InventoryPaneListener actionListener = new InventoryPaneListener(this);
        JMenuItem equipItem = new JMenuItem("Equip");
        equipItem.addActionListener(getGeneralListener());
        JMenuItem dropItem = new JMenuItem("Drop");
        dropItem.addActionListener(getGeneralListener());
        menu.add(equipItem);
        menu.add(dropItem);
        this.setComponentPopupMenu(menu);
        menu.addMouseListener(new DialogPopupListener(this));
    }
}
