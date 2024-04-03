package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class Food extends AbstractItem
{
    private int nutritionValue;

    public Food()
    {

    }

    @Override
    public BufferedImage getItemImage()
    {
        return null;
    }

}
