package net.ck.mtbg.ui.buttons;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class CancelButton extends JButton implements MouseListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private boolean hovered;
    private String label = "Cancel";

	public CancelButton()
	{
		setIcon(ImageUtils.createImageIcon(GameConfiguration.miscImages + "BUTTONS" + File.separator + "cleanButton.png", ""));
        this.setFont(getFont());
        setText(label);
		//this.setToolTipText(getLogger().getName());
        this.setActionCommand(label);
        this.addActionListener(WindowBuilder.getController());
        hovered = false;
		this.addMouseListener(this);
		this.setVisible(true);
		
	}

	
	protected void paintBorder(Graphics g)
	{
		super.paintBorder(g);
		//g.setColor(Color.red);
		//g.drawRect(0, 0, getWidth(), getHeight());
		
	}

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
        Rectangle2D r = fm.getStringBounds(label, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(label, x, y);
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
