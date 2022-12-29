package net.ck.game.ui.renderers;

import net.ck.game.items.AbstractItem;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html#renderer
 * https://docs.oracle.com/javase/tutorial/uiswing/components/list.html#mutable
 * 
 * @author Claus
 *
 */
public class InventoryPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractItem>
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public InventoryPaneListCellRenderer()
	{
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.LEFT);
		setVerticalAlignment(SwingConstants.CENTER);
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
