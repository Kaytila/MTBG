package net.ck.game.ui.components;

import net.ck.game.backend.game.Game;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.WeatherChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * 
 * @author Claus weather canvas is the UI element where the current weather is painted on. Notification is done via EventBus, canvas just paints Currently, the notification only contains the message,
 *         not the weather, so weather is done via constructor. Not sure yet whether I like this or whether passing the weather object via the bus would not be more suitable
 */
public class JWeatherCanvas extends JPanel
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private BufferedImage image;

	public JWeatherCanvas()
	{
		this.setBackground(Color.DARK_GRAY);
		this.setBounds(GameConfiguration.UIwidth - 220, 0, 150, 150);
		Border blackline = BorderFactory.createLineBorder(Color.MAGENTA);
		this.setBorder(blackline);
		this.setFocusable(false);
		setImage(Game.getCurrent().getCurrentMap().getWeather().getWeatherImage());
		EventBus.getDefault().register(this);
		this.setVisible(true);
		//this.setToolTipText(getLogger().getName());
		this.repaint();
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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


	/**
	 * get the image, resize as appropriate and draw on the screen, hopefully to scale.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		BufferedImage img = Game.getCurrent().getCurrentMap().getWeather().getWeatherImage();
		if (img != null)
		{
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
		}
	}

	@Subscribe // (threadMode = ThreadMode.MAIN)
	public void onMessageEvent(WeatherChangedEvent event)
	{
		this.repaint();
	}

}
