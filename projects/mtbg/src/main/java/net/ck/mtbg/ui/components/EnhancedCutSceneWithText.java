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

public class EnhancedCutSceneWithText extends SimpleCutScene
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private ArrayList<BufferedImage> images;
    private ArrayList<String> textMessages;
    private int counter;
    private Timer counterTimer;


    /**
     * https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
     *
     * @param img
     */
    public EnhancedCutSceneWithText(ArrayList<BufferedImage> img, ArrayList<String> texts)
    {
        images = img;
        counter = 0;
        textMessages = texts;

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
                        SwingUtilities.invokeAndWait(() -> EnhancedCutSceneWithText.this.repaint());
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
                    JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor(EnhancedCutSceneWithText.this);
                    f2.dispatchEvent(new WindowEvent(f2, WindowEvent.WINDOW_CLOSING));

                }
            }
        };
        counterTimer.schedule(task, 5000, 5000);
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
        logger.debug("draw image number: {}", counter);
        g.drawImage(images.get(counter), 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);

        Font font = new Font("Arial", Font.BOLD, 25);

        logger.debug("get text: {}", getTextMessages().get(counter));
        AttributedString attributedText = new AttributedString(getTextMessages().get(counter));
        attributedText.addAttribute(TextAttribute.FONT, font);
        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.GREEN);

        Graphics g1 = (images.get(counter)).getGraphics();

        FontMetrics metrics = g1.getFontMetrics(font);
        //int positionX = (getImage().getWidth() - metrics.stringWidth(getTextMessage()));
        int positionX = 10;
        int positionY = (GameConfiguration.UIheight - metrics.getHeight() - 40) + metrics.getAscent();

        g1.drawString(attributedText.getIterator(), positionX, positionY);
        g1.dispose();
    }

    public ArrayList<String> getTextMessages()
    {
        return textMessages;
    }

    public void setTextMessages(ArrayList<String> textMessages)
    {
        this.textMessages = textMessages;
    }
}
