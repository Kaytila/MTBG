package net.ck.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import net.ck.game.backend.Game;
import net.ck.util.communication.graphics.WeatherChangedEvent;

/**
 * 
 * @author Claus weather canvas is the UI element where the current weather is painted on. Notification is done via EventBus, canvas just paints Currently, the notification only contains the message,
 *         not the weather, so weather is done via constructor. Not sure yet whether I like this or whether passing the weather object via the bus would not be more suitable
 */
public class JWeatherCanvas extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private BufferedImage image;

	public JWeatherCanvas()
	{
		this.setBackground(Color.DARK_GRAY);
		this.setBounds(700 - 220, 0, 150, 150);
		Border blackline = BorderFactory.createLineBorder(Color.MAGENTA);
		this.setBorder(blackline);
		this.setFocusable(false);
		setImage(Game.getCurrent().getCurrentMap().getCurrentWeather().getWeatherImage());
		EventBus.getDefault().register(this);
		this.setVisible(true);
		this.setToolTipText(getLogger().getName());
		this.repaint();
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public BufferedImage getImage()
	{
		return this.image;
	}

	public void setImage(BufferedImage img)
	{
		if (getImage() != null)
		{
			if (!(getImage().equals(img)))
			{
				this.image = img;
				this.repaint();
			}
		}
		else
		{
			this.image = img;
			this.repaint();
		}
	}

	@Override
	/**
	 * get the image, resize as appropriate and draw on the screen, hopefully to scale.
	 */
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		BufferedImage img = Game.getCurrent().getCurrentMap().getCurrentWeather().getWeatherImage();
		if (img != null)
		{
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
		}
		/*
		 * if (getImage() != null) { g.drawImage(getImage(),0, 0, this.getWidth(), this.getHeight(), 0 , 0, getImage().getWidth(), getImage().getHeight(), null);
		 * //logger.info("trying to draw image rectangle: {} {} onto canvas: {}", getImage().getWidth(), getImage().getHeight(), this.getBounds()); //g.drawImage(getImage(), 0, 0, this); }
		 */
	}

	@Subscribe // (threadMode = ThreadMode.MAIN)
	public void onMessageEvent(WeatherChangedEvent event)
	{
		//logger.info("Weather {} image has changed", Game.getCurrent().getCurrentMap().getCurrentWeather());
		// setImage(Game.getCurrent().getCurrentMap().getCurrentWeather().getWeatherImage());
		this.repaint();
	}

	public Logger getLogger()
	{
		return logger;
	}
}
