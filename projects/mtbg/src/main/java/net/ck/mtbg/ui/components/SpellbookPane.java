package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.listeners.SpellBookListener;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class SpellbookPane extends JList<AbstractSpell>
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private Point selectionPoint;

	public SpellbookPane()
	{
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation(VERTICAL_WRAP);
		this.requestFocus();
		this.setVisibleRowCount(-1);
		setBounds(20, 40, 200, 100);
		SpellBookListener spellBookListener = new SpellBookListener();
		this.addMouseListener(spellBookListener);
		this.addMouseMotionListener(spellBookListener);
		this.setModel(Game.getCurrent().getCurrentPlayer().getSpells());
	}

	public Point getSelectionPoint()
	{
		return selectionPoint;
	}

	public void setSelectionPoint(Point selectionPoint)
	{
		this.selectionPoint = selectionPoint;
	}
}
