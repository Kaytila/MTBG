package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

public class SimpleCutSceneWithText extends SimpleCutScene
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private String textMessage;

    public SimpleCutSceneWithText(BufferedImage img, String text)
    {
        super(img);
        textMessage = text;
    }

    public String getTextMessage()
    {
        return textMessage;
    }

    public void setTextMessage(String textMessage)
    {
        this.textMessage = textMessage;
    }

    public void paintComponent(Graphics g)
    {
        logger.debug("draw image");
        g.drawImage(getImage(), 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);
        logger.debug("draw text: ", this::getTextMessage);

        Font font = new Font("Arial", Font.BOLD, 25);

        AttributedString attributedText = new AttributedString(getTextMessage());
        attributedText.addAttribute(TextAttribute.FONT, font);
        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.GREEN);

        Graphics g1 = getImage().getGraphics();

        FontMetrics metrics = g1.getFontMetrics(font);
        //int positionX = (getImage().getWidth() - metrics.stringWidth(getTextMessage()));
        int positionX = 10;
        int positionY = (GameConfiguration.UIheight - metrics.getHeight() - 40) + metrics.getAscent();

        g.drawString(attributedText.getIterator(), positionX, positionY);
        g1.dispose();
    }
}







