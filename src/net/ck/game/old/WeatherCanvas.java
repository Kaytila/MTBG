package net.ck.game.old;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import net.ck.game.backend.Game;
import net.ck.game.weather.Weather;
import net.ck.util.communication.graphics.WeatherChangedEvent;

/**
 * 
 * @author Claus weather canvas is the UI element where the current weather is painted on. Notification is done via EventBus, canvas just paints Currently, the notification only contains the message,
 *         not the weather, so weather is done via constructor. Not sure yet whether I like this or whether passing the weather object via the bus would not be more suitable
 */
public class WeatherCanvas extends Canvas
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private BufferedImage image;
	private Weather weather;

	public WeatherCanvas(Weather weather)
	{
		this.setBounds(250, 0, 100, 100);
		this.setVisible(true);
		this.setFocusable(false);
		this.weather = weather;
		EventBus.getDefault().register(this);
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

	public void setImage(BufferedImage image)
	{
		// should reduce flashing and repaints
		if (getImage() != null)
		{
			if (!(getImage().equals(image)))
			{
				this.image = image;
				this.repaint();
			}
		}
		else
		{
			this.image = image;
			this.repaint();
		}
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		// logger.info("running paint method");
		if (getImage() != null)
		{
			// logger.info("image {}", image.toString());
			g.drawImage(image, 250, 0, this);

		}
		/*
		 * else { logger.error("image {} not available", image.toString()); }
		 */
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(WeatherChangedEvent event)
	{
		// logger.info("Weather image has changed - so I catch this in WeatherCanvas and repaint");
		setImage(weather.getWeatherImage());
	}

}
