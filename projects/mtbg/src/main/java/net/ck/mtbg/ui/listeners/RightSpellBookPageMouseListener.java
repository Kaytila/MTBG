package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.components.SpellbookPane;
import net.ck.mtbg.ui.models.SpellBookListDataModel;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class RightSpellBookPageMouseListener implements MouseListener, MouseMotionListener, MouseInputListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private SpellbookPane spellbookPane;

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	public RightSpellBookPageMouseListener(SpellbookPane spellbookPane)
	{
		this.spellbookPane = spellbookPane;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		logger.debug("mouse clicked on right page");
		if (Game.getCurrent().getCurrentPlayer().increaseLevel())
		{
			((SpellBookListDataModel) spellbookPane.getModel()).filterSpellsByLevel();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{

	}
}
