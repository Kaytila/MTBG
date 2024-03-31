package net.ck.mtbg.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class ImagePair
{
    private float percentage;
    private BufferedImage sourceImage;
    private BufferedImage resultImage;
    private String hash;

    public ImagePair(float percentage, BufferedImage sourceImage, BufferedImage resultImage)
    {
        this.percentage = percentage;
        this.sourceImage = sourceImage;
        this.resultImage = resultImage;
        this.hash = percentage + sourceImage.toString();
    }


}
