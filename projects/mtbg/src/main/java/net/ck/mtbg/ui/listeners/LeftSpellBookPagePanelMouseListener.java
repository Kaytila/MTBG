package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.backend.state.NoiseManager;
import net.ck.mtbg.soundeffects.SoundEffects;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class LeftSpellBookPagePanelMouseListener implements MouseListener, MouseMotionListener, MouseInputListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		logger.debug("mouse clicked on left page");
		NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.PAGETURN);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		logger.info("mouse entered component: {}", e.getComponent());
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		logger.info("mouse leave component: {}", e.getComponent());
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
