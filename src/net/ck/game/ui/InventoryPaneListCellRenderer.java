package net.ck.game.ui;

import net.ck.game.items.AbstractItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html#renderer
 * https://docs.oracle.com/javase/tutorial/uiswing/components/list.html#mutable
 * 
 * @author Claus
 *
 */
public class InventoryPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractItem>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InventoryPaneListCellRenderer()
	{
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.LEFT);
		setVerticalAlignment(SwingConstants.CENTER);
	}

	private final Logger logger = LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractItem> list, AbstractItem value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		// Set the icon and text. If icon was null, say so.
		ImageIcon icon = new ImageIcon(value.getItemImage());
		String text = value.getName();
		setIcon(icon);
		if (icon != null)
		{
			setText(text);
			setFont(list.getFont());
		}
		return this;
	}
}
