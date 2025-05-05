package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.communication.graphics.WeatherChangedEvent;
import net.ck.mtbg.weather.Weather;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Claus weather canvas is the UI element where the current weather is painted on. Notification is done via EventBus, canvas just paints Currently, the notification only contains the message,
 * not the weather, so weather is done via constructor. Not sure yet whether I like this or whether passing the weather object via the bus would not be more suitable
 */
@Log4j2
@Getter
@Setter
public class JWeatherCanvas extends JPanel
{
    private BufferedImage image;

    public JWeatherCanvas()
    {
        this.setBackground(Color.DARK_GRAY);
        this.setBounds(GameConfiguration.UIwidth - 220, 0, 150, 150);
        Border blackline = BorderFactory.createLineBorder(Color.MAGENTA);
        this.setBorder(blackline);
        this.setFocusable(false);
        if (Game.getCurrent().getCurrentMap().getWeather() != null)
        {
            setImage(Game.getCurrent().getCurrentMap().getWeather().getWeatherImage());
        }
        else
        {
            Game.getCurrent().getCurrentMap().setWeather(new Weather());
        }
        EventBus.getDefault().register(this);
        this.setVisible(true);
        //this.setToolTipText(getLogger().getName());
        this.repaint();
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
