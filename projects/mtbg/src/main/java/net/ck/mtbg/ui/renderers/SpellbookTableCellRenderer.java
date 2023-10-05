package net.ck.mtbg.ui.renderers;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SpellbookTableCellRenderer extends JLabel implements TableCellRenderer
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public SpellbookTableCellRenderer()
	{
		this.setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		logger.debug("render method start");
		Color newColor = Color.BLUE;
		setBackground(newColor);
		if (isSelected)
		{
			setBorder(BorderFactory.createLineBorder(Color.PINK, 3));
			table.setSelectionBackground(Color.GREEN);
		}
		else
		{

			//unselectedBorder is a solid border in the color
			//table.getBackground().
			setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
			table.setSelectionBackground(Color.ORANGE);
		}
		setToolTipText(value.toString());
		return this;
	}
}
