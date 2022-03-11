package net.ck.game.ui;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.Inventory;
import net.ck.game.items.AbstractItem;
import net.ck.util.GameUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;

public class InventoryPane extends JList<AbstractItem>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JScrollPane sp;
	private JPopupMenu menu;
	private ListSelectionModel listSelectionModel;
	private int selectionIndex;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
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
		this.setFont(GameUtils.getFont());
		
		this.setAutoscrolls(true);
		this.setBorder(blackline);
		this.setToolTipText(getLogger().getName());
		this.requestFocus();
		// one way of doing drag and drop
		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON);
		this.setTransferHandler(new InventoryPaneTransferHandler());
		 
		/** this is another way to do drag and drop
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



	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
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
	
	public Logger getLogger()
	{
		return logger;
	}

	public int getSelectionIndex()
	{
		return selectionIndex;
	}

	/**
	 * kind of breaking MVC here, but I need to know what item is selected without having to go back to the list selection model
	 * @param selectionIndex - the 0-based index of the selected item in the list
	 */
	public void setSelectionIndex(int selectionIndex)
	{
		this.selectionIndex = selectionIndex;
	}

	public InventoryPaneListener getGeneralListener()
	{
		return generalListener;
	}

	public void setGeneralListener(InventoryPaneListener generalListener)
	{
		this.generalListener = generalListener;
	}

	public Frame getOwner()
	{
		return owner;
	}

	public void setOwner(Frame owner)
	{
		this.owner = owner;
	}

	public AbstractDialog getParentDialog()
	{
		return parentDialog;
	}

	public void setParentDialog(AbstractDialog parentDialog)
	{
		this.parentDialog = parentDialog;
	}
}
