package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageUtils;
import org.apache.commons.lang3.Range;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class Weapon extends AbstractItem
{
    private WeaponTypes type;
    private WeaponDamageTypes damageType;
    private Range<Integer> weaponDamage;
    private double averageDamage;
    private int range;

    public Weapon(Weapon that)
    {
        this(that.getId(), that.getName(), that.getItemImage(), that.getAverageDamage(), that.getDamageType(), that.getRange(), that.getWeaponDamage(), that.getAdditionalEffects(), that.getValue(), that.getWeight());
    }

    public Weapon(int id, String name, BufferedImage itemImage, double averageDamage, WeaponDamageTypes damageType, int range, Range<Integer> weaponDamage, ArrayList<Effects> additionalEffects, double value, double weight)
    {
        this.setId(id);
        this.setName(name);
        this.setAverageDamage(averageDamage);
        this.setDamageType(damageType);
        this.setRange(range);
        this.setWeaponDamage(weaponDamage);
        this.setAdditionalEffects(additionalEffects);
        this.setValue(value);
        this.setWeight(weight);
        setContainer(false);
    }

    public Weapon()
    {
        setContainer(false);
    }

    public Weapon(int iD)
    {
        setContainer(false);
    }


    public int getRange()
    {
        if (getType().equals(WeaponTypes.MELEE))
        {
            return 1;
        }
        return range;
    }

    @Override
    public BufferedImage getItemImage()
    {
        return (ImageUtils.loadImage("weapons" + File.separator, getName()));
    }

}
