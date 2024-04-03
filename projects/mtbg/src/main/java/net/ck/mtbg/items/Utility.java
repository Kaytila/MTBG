package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.util.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class Utility extends AbstractItem
{
    private Double capacity;
    private Inventory inventory;

    public Utility()
    {
        super();
        inventory = new Inventory();
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Utility(Utility that)
    {
        this(that.getId(), that.getName(), that.getCapacity(), that.getInventory(), that.getAdditionalEffects(), that.getValue(), that.getWeight());
    }

    public Utility(int id, String name, Double capacity, Inventory inventory, ArrayList<Effects> additionalEffects, double value, double weight)
    {
        this.setId(id);
        this.setName(name);
        this.setCapacity(capacity);
        this.setInventory(inventory);
        this.setAdditionalEffects(additionalEffects);
        this.setValue(value);
        this.setWeight(weight);
        this.setContainer(true);
    }

    public BufferedImage getItemImage()
    {
        return (ImageUtils.loadImage("utilities" + File.separator, getName()));
    }
}
