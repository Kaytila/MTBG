package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;

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

@Getter
@Setter
@Log4j2
@ToString
public class EnhancedCutSceneWithText extends SimpleCutScene
{
    protected final Font font = new Font("Arial", Font.BOLD, 25);
    final protected ArrayList<AttributedString> attributeString = new ArrayList<>();
    protected ArrayList<BufferedImage> images;
    protected int counterImages;
    protected Timer counterTimer;

    /**
     * <a href="https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe">...</a>
     *
     * @param img   - the list of images used for the cutscene
     * @param texts - the list of texts used for the cutscene
     */
    public EnhancedCutSceneWithText(ArrayList<BufferedImage> img, ArrayList<String> texts)
    {
        if (img.size() != texts.size())
        {
            logger.error("number of images and texts does not match");
            Game.getCurrent().stopGame();
        }

        images = img;
        counterImages = 0;


        for (String txt : texts)
        {
            AttributedString str = new AttributedString(txt);
            str.addAttribute(TextAttribute.FONT, font);
            str.addAttribute(TextAttribute.FOREGROUND, Color.GREEN);
            attributeString.add(str);
        }
        logger.info("finished setup");
        counterTimer = new Timer();
        //TODO test - could work? should work!
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                //logger.info("counter 1: {}", counter);
                if ((counterImages + 1) < images.size())
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
                    //logger.info("counter 2: {}", counter);
                    counterImages++;
                    //logger.info("counter 3: {}", counter);

                }
                else
                {
                    logger.info("this is the end");
                    //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                    JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor(EnhancedCutSceneWithText.this);
                    f2.dispatchEvent(new WindowEvent(f2, WindowEvent.WINDOW_CLOSING));
                    counterTimer.cancel();
                }
            }
        };
        counterTimer.schedule(task, 5000, 5000);
    }

    public EnhancedCutSceneWithText()
    {
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

        g.drawString(attributeString.get(counterImages).getIterator(), positionX, positionY);
        g1.dispose();
    }
}
