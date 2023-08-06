package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <a href="https://docs.oracle.com/javase/6/docs/api/javax/swing/SwingUtilities.html#getAncestorOfClass%28java.lang.Class,%20java.awt.Component%29">
 * https://docs.oracle.com/javase/6/docs/api/javax/swing/SwingUtilities.html#getAncestorOfClass%28java.lang.Class,%20java.awt.Component%29
 * </a>
 */
public class EnhancedCutSceneWithDynamicText extends EnhancedCutSceneWithText
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private final int imageRolloverDelay = 5000;
    private final int imageRolloverPeriod = 5000;
    private int counterCharacters = 0;
    private ArrayList<String> textStrings;
    private String currentText;
    private Timer counterTimerText;
    private Timer counterTimer;

    public EnhancedCutSceneWithDynamicText(ArrayList<BufferedImage> img, ArrayList<String> texts)
    {
        images = img;
        counterImages = 0;
        textStrings = texts;

        createTextTimer();
        createImageTimer();
    }

    private void createImageTimer()
    {
        counterTimer = new Timer();
        //TODO test - could work? should work!
        TimerTask taskImages = new TimerTask()
        {
            @Override
            public void run()
            {
                //logger.info("counter 1: {}", counter);
                if ((counterImages + 1) < images.size())
                {
                    try
                    {
                        SwingUtilities.invokeAndWait(() -> EnhancedCutSceneWithDynamicText.this.repaint());
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                    //logger.info("counter 2: {}", counter);
                    counterImages++;
                    counterCharacters = 0;
                    createTextTimer();
                    //logger.info("counter 3: {}", counter);

                }
                else
                {
                    logger.info("this is the end");
                    //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                    JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor(EnhancedCutSceneWithDynamicText.this);
                    f2.dispatchEvent(new WindowEvent(f2, WindowEvent.WINDOW_CLOSING));
                    counterTimer.cancel();
                    counterTimerText.cancel();
                }
            }
        };
        counterTimer.schedule(taskImages, imageRolloverDelay, imageRolloverDelay);
    }

    public ArrayList<BufferedImage> getImages()
    {
        return images;
    }

    public void setImages(ArrayList<BufferedImage> images)
    {
        this.images = images;
    }


    public void paintComponent(Graphics g)
    {
        logger.debug("draw image number: {}", counterImages);
        g.drawImage(images.get(counterImages), 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);

        Graphics g1 = (images.get(counterImages)).getGraphics();

        FontMetrics metrics = g1.getFontMetrics(font);
        //int positionX = (getImage().getWidth() - metrics.stringWidth(getTextMessage()));
        int positionX = 10;
        int positionY = (GameConfiguration.UIheight - metrics.getHeight() - 40) + metrics.getAscent();

        String currentString = textStrings.get(counterImages).substring(0, counterCharacters + 1);
        logger.debug("draw current text: {}", currentString);

        AttributedString str = new AttributedString(currentString);
        str.addAttribute(TextAttribute.FONT, font);
        str.addAttribute(TextAttribute.FOREGROUND, Color.GREEN);

        g.drawString(str.getIterator(), positionX, positionY);
        g1.dispose();
    }

    private void createTextTimer()
    {
        counterTimerText = new Timer();
        TimerTask taskText = new TimerTask()
        {
            @Override
            public void run()
            {
                //logger.info("counter 1: {}", counter);
                if ((counterCharacters + 1) < textStrings.get(counterImages).length())
                {
                    try
                    {
                        SwingUtilities.invokeAndWait(() -> EnhancedCutSceneWithDynamicText.this.repaint());
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                    //logger.info("counter 2: {}", counter);
                    counterCharacters++;
                    //logger.info("counter 3: {}", counter);

                }
                else
                {
                    logger.info("this is the end");
                    //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                    //JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor(EnhancedCutSceneWithDynamicText.this);
                    //f2.dispatchEvent(new WindowEvent(f2, WindowEvent.WINDOW_CLOSING));
                    counterTimerText.cancel();
                }
            }
        };

        counterTimerText.schedule(taskText, ((GameConfiguration.cutSceneImageRolloverDelay - GameConfiguration.cutSceneImageRollOverBuffer) / textStrings.get(counterImages).length()), ((GameConfiguration.cutSceneImageRolloverPeriod - GameConfiguration.cutSceneImageRollOverBuffer) / textStrings.get(counterImages).length()));
    }
}

