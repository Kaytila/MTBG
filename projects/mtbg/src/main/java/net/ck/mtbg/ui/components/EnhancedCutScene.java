package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Log4j2
@Getter
@Setter
public class EnhancedCutScene extends SimpleCutScene
{

    final private Timer counterTimer;
    private ArrayList<BufferedImage> images;
    private int counter;


    /**
     * https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
     *
     * @param img
     */
    public EnhancedCutScene(ArrayList<BufferedImage> img)
    {
        images = img;
        counter = 0;

        counterTimer = new Timer();
        //TODO test - could work? should work!
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                logger.info("counter 1: {}", counter);
                if ((counter + 1) < images.size())
                {
                    try
                    {
                        SwingUtilities.invokeAndWait(() -> EnhancedCutScene.this.repaint());
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                    logger.info("counter 2: {}", counter);
                    counter++;
                    logger.info("counter 3: {}", counter);

                }
                else
                {
                    logger.info("this is the end");
                    //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                    JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor(EnhancedCutScene.this);
                    f2.dispatchEvent(new WindowEvent(f2, WindowEvent.WINDOW_CLOSING));

                }
            }
        };
        counterTimer.schedule(task, 5000, 5000);
    }

    public void paintComponent(Graphics g)
    {
        logger.debug("draw image number: {}", counter);
        g.drawImage(images.get(counter), 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);
    }
}
