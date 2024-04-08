package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageUtils;

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

    public Food(Food that)
    {
        this(that.getName(), that.getId(), that.getValue(), that.getWeight(), that.getNutritionValue());
    }

    public Food(String name, int id, double value, double weight, int nutritionValue)
    {
        this.setId(id);
        this.setName(name);
        this.setValue(value);
        this.setWeight(weight);
        this.setNutritionValue(nutritionValue);
        this.setFurniture(false);
    }

    @Override
    public BufferedImage getItemImage()
    {
        return ImageUtils.loadImage("food", getName());
    }

}
