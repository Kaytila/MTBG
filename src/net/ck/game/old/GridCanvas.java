package net.ck.game.old;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;

public class GridCanvas extends Canvas implements KeyListener, FocusListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final String newline = System.getProperty("line.separator");

	private BufferedImage image;
	private ArrayList<BufferedImage> images;

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	
	public GridCanvas(Game game)
	{
		logger.error("do not use me");
		System.exit(-1);
		EventBus.getDefault().register(this);
		setFocusable(true);
		setBackground(Color.green);
		requestFocus();
	}

	/*
	 * old
	 * 
	 * @Override public void paint(Graphics g) { super.paint(g);
	 * logger.error("running paint method"); //Toolkit t =
	 * Toolkit.getDefaultToolkit(); // test test test if (game != null) { if
	 * (game.getPlayers() != null) { if (game.getPlayers().get(0) != null) { if
	 * (game.getPlayers().get(0).getAppearance() != null) { if
	 * (game.getPlayers().get(0).getAppearance().getCurrentImage() != null) {
	 * logger.error("finally, there is an image!"); //image =
	 * (game.getPlayers().get(0).getAppearance().getCurrentImage());
	 * logger.error("image {}", image.toString()); g.drawImage(image, 120, 100,
	 * this); } else {
	 * logger.error("player appearance does not have an image!"); } } else {
	 * logger.error("player does not have an apperance"); } } else {
	 * logger.error("player 0 does not exist"); } } else {
	 * logger.error("there is no list of players"); } } else {
	 * logger.error("there is no game!"); }
	 */

	/*
	 * old
	 * 
	 * @Override public void paint(Graphics g) { super.paint(g); int rowW = 16;
	 * int rowH = 16; int rows = this.getHeight() / 16; int cols =
	 * this.getWidth() / 16; int i = 0; for (i = 0; i < rows; i++) {
	 * g.drawLine(0, i * rowH, this.getWidth(), i * rowH); }
	 * 
	 * for (i = 0; i < cols; i++) { g.drawLine(i * rowW, 0, i * rowW,
	 * this.getHeight()); }
	 * 
	 * if (getImage() != null) { g.drawImage(image, 32, 0, this); } }
	 */

	public void paint(Graphics g)
	{
		super.paint(g);
		int rows = this.getHeight() / 32;
		int cols = this.getWidth() / 32;
		if (getImage() != null)
		{
			//logger.info("image {}", image.toString());
			g.drawImage(image, 32, 0, this);
		}
	}

	public BufferedImage getImage()
	{
		return this.image;
	}

	public void setImage(BufferedImage image)
	{
		if (image != null)
		{
			// should reduce flashing and repaints
			if (getImage() != null)
			{
				if (!(getImage().equals(image)))
				{
					this.image = image;
					this.repaint(32, 0, 16, 16);
				}
			} else
			{
				this.image = image;
				this.repaint(32, 0, 16, 16);
			}
		} else
		{
			logger.info("no image - how and why and ...");
		}
	}

	@Subscribe
	public void onMessageEvent(AnimatedRepresentationChanged event)
	{
		logger.info("player image has changed - so I catch this in GridCanvas and repaint");
		// TODO
		// have player position added here and update the position on the grid
		// accordingly.
		setImage(event.getPlayer().getAppearance().getCurrentImage());

	}

	private Icon createIcon()
	{
		int width = 32;
		BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		Color color = new Color(255, 255, 255, 0);
		g.setColor(color);
		g.fillRect(0, 0, width, width);
		g.dispose();
		return new ImageIcon(img);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		displayInfo(e, "KEY TYPED: ");
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		displayInfo(e, "KEY PRESSED: ");
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		displayInfo(e, "KEY RELEASED: ");
	}

	private void displayInfo(KeyEvent e, String keyStatus)
	{

		// You should only rely on the key char if the event
		// is a key typed event.
		int id = e.getID();
		String keyString;
		if (id == KeyEvent.KEY_TYPED)
		{
			char c = e.getKeyChar();
			keyString = "key character = '" + c + "'";
		} else
		{
			int keyCode = e.getKeyCode();
			keyString = "key code = " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")";
		}

		int modifiersEx = e.getModifiersEx();
		String modString = "extended modifiers = " + modifiersEx;
		String tmpString = KeyEvent.getModifiersExText(modifiersEx);
		if (tmpString.length() > 0)
		{
			modString += " (" + tmpString + ")";
		} else
		{
			modString += " (no extended modifiers)";
		}

		String actionString = "action key? ";
		if (e.isActionKey())
		{
			actionString += "YES";
		} else
		{
			actionString += "NO";
		}

		String locationString = "key location: ";
		int location = e.getKeyLocation();
		if (location == KeyEvent.KEY_LOCATION_STANDARD)
		{
			locationString += "standard";
		} else if (location == KeyEvent.KEY_LOCATION_LEFT)
		{
			locationString += "left";
		} else if (location == KeyEvent.KEY_LOCATION_RIGHT)
		{
			locationString += "right";
		} else if (location == KeyEvent.KEY_LOCATION_NUMPAD)
		{
			locationString += "numpad";
		} else
		{ // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
			locationString += "unknown";
		}

		logger.info(keyStatus + newline + "    " + keyString + newline + "    " + modString
			+ newline + "    " + actionString + newline + "    " + locationString + newline);
		// displayArea.setCaretPosition(displayArea.getDocument().getLength());
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		logger.info("gained focus");
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		logger.info("lost focus");
		
	}

}