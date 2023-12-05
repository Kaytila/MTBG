package net.ck.mtbg.ui.renderers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class SpellbookListCellRenderer extends JLabel implements ListCellRenderer<AbstractSpell>
{


	public SpellbookListCellRenderer() {
		this.setOpaque(true);
	}


	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractSpell> list, AbstractSpell value, int index, boolean isSelected, boolean cellHasFocus) {
		setPreferredSize(new Dimension(100, 40));
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		//logger.debug("spell book list: {}", value.toString());
		String text = value.getName();
		if (value.getMenuImage() != null) {
			ImageIcon icon = new ImageIcon(value.getMenuImage());
			setIcon(icon);
		} else {
			logger.debug("Spell has no image: {}", value.getName());
		}
		//setText(text);
		//setFont(list.getFont());
		return this;
	}
}
