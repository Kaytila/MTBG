package net.ck.mtbg.ui.renderers;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class SpellbookListCellRenderer extends JLabel implements ListCellRenderer<AbstractSpell>
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public SpellbookListCellRenderer()
	{
		this.setOpaque(true);
	}


	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractSpell> list, AbstractSpell value, int index, boolean isSelected, boolean cellHasFocus)
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

		logger.debug("spell book list: {}", value.toString());
		String text = value.getName();
		if (value.getMenuImage() != null)
		{
			ImageIcon icon = new ImageIcon(value.getMenuImage());
			setIcon(icon);
		}
		setText(text);
		setFont(list.getFont());
		return this;
	}
}
