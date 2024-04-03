package net.ck.mtbg.items;

import net.ck.mtbg.util.utils.CodeUtils;
import net.ck.mtbg.util.utils.ImageUtils;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Weapon extends AbstractItem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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
    }

    public Weapon()
    {
        setContainer(false);
    }

    public Weapon(int iD)
    {
        setContainer(false);
    }

    @Override
    public String toString()
    {
        //return "Weapon [type=" + type + ", damageType=" + damageType + ", weaponDamage=" + weaponDamage + ", averageDamage=" + averageDamage + ", toString()=" + super.toString() + "]";
        return "Weapon: " + super.toString();
    }

    public int getRange()
    {
        if (getType().equals(WeaponTypes.MELEE))
        {
            return 1;
        }
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public WeaponTypes getType()
    {
        return type;
    }

    public void setType(WeaponTypes type)
    {
        this.type = type;
    }

    public double getAverageDamage()
    {
        return averageDamage;
    }

    public void setAverageDamage(double averageDamage)
    {
        this.averageDamage = averageDamage;
    }


    @Override
    public BufferedImage getItemImage()
    {
        return (ImageUtils.loadImage("weapons" + File.separator, getName()));
    }

    public Range<Integer> getWeaponDamage()
    {
        return weaponDamage;
    }

    public void setWeaponDamage(Range<Integer> weaponDamage)
    {
        this.weaponDamage = weaponDamage;
    }

    public WeaponDamageTypes getDamageType()
    {
        return damageType;
    }

    public void setDamageType(WeaponDamageTypes damageType)
    {
        this.damageType = damageType;
    }
}
