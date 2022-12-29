package net.ck.game.ui.buttons;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class OKButton extends JButton implements MouseListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private boolean hovered;

	public OKButton()
	{	
		setIcon(ImageUtils.createImageIcon(ImageUtils.getAdditionalimagespath() + File.separator + "BUTTONS" + File.separator + "cleanButton.png", ""));
		this.setFont(getFont());
		setText("OK");
		//this.setToolTipText(getLogger().getName());
		this.setActionCommand("OK");
		this.addActionListener(Game.getCurrent().getController());
		this.setVisible(true);
		hovered = false;
		this.addMouseListener(this);
	}

	/*
	protected void paintBorder(Graphics g)
	{
		super.paintBorder(g);
	}
*/
	/**
	 * with a little help from stackoverflow again
	 * 
	 * https://stackoverflow.com/questions/14284754/java-center-text-in-rectangle/14287270#14287270
	 */
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (hovered)
		{	
			g.setColor(Color.white);			
		}
		else
		{
			g.setColor(Color.black);
		}
		
		Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds("OK", g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString("OK", x, y);
	}

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

		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{	
		hovered = true;
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		hovered = false;
	}

}
