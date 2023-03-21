package net.ck.mtbg.items;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class Food extends AbstractItem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private int nutritionValue;


    public Food()
    {

    }

    @Override
    public BufferedImage getItemImage()
    {
        return null;
    }

    public int getNutritionValue()
    {
        return nutritionValue;
    }

    public void setNutritionValue(int nutritionValue)
    {
        this.nutritionValue = nutritionValue;
    }
}
