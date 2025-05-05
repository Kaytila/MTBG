package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

@Log4j2
@Getter
@Setter
public class SimpleCutSceneWithText extends SimpleCutScene
{
    private String textMessage;

    public SimpleCutSceneWithText(BufferedImage img, String text)
    {
        super(img);
        textMessage = text;
    }

    public void paintComponent(Graphics g)
    {
        logger.debug("draw image");
        g.drawImage(getImage(), 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);
        logger.debug("draw text: {}", getTextMessage());

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
