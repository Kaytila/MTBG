package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponDamageTypes;
import net.ck.mtbg.items.WeaponTypes;
import org.apache.commons.lang3.Range;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class WeaponXMLReader extends DefaultHandler
{

    private Hashtable<Integer, Weapon> weaponList;

    private StringBuilder data;

    private boolean weight;

    private boolean value;

    private boolean weapondamage;

    private boolean damage;

    private boolean type;

    private boolean id;

    private Weapon weapon;

    private boolean item;

    private boolean weapons;
    private boolean name;
    private boolean image;

    private boolean range;

    @Override
    public void startDocument()
    {
        weaponList = new Hashtable<Integer, Weapon>();
    }

    @Override
    public void endDocument()
    {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        //logger.info("qName: {}", qName);

        if (qName.equalsIgnoreCase("image"))
        {
            image = true;
        }

        if (qName.equalsIgnoreCase("name"))
        {
            name = true;
        }

        if (qName.equalsIgnoreCase("weapons"))
        {
            weapons = true;

        }

        if (qName.equalsIgnoreCase("weapon"))
        {
            item = true;
            weapon = new Weapon();
        }

        if (qName.equalsIgnoreCase("id"))
        {
            id = true;

        }

        if (qName.equalsIgnoreCase("type"))
        {
            type = true;

        }

        if (qName.equalsIgnoreCase("damage"))
        {
            damage = true;

        }

        if (qName.equalsIgnoreCase("weaponDamage"))
        {
            weapondamage = true;

        }

        if (qName.equalsIgnoreCase("value"))
        {
            value = true;

        }

        if (qName.equalsIgnoreCase("weight"))
        {
            weight = true;

        }

        if (qName.equalsIgnoreCase("range"))
        {
            range = true;
        }

        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if (weapons)
        {
            weapons = false;
        }

        if (id)
        {
            id = false;
            weapon.setId(Integer.parseInt(data.toString()));
        }

        if (item)
        {
            item = false;
            weaponList.put(weapon.getId(), weapon);
        }
        else if (type)
        {
            type = false;
            weapon.setType(WeaponTypes.valueOf(data.toString()));
        }

        else if (damage)
        {
            damage = false;
            weapon.setDamageType(WeaponDamageTypes.valueOf(data.toString()));
        }

        else if (weapondamage)
        {
            weapondamage = false;
            String[] damages = data.toString().split("-");
            Range<Integer> range = Range.between(Integer.valueOf(damages[0]), Integer.valueOf(damages[1]));
            weapon.setWeaponDamage(range);
        }

        else if (value)
        {
            value = false;
            weapon.setValue(Double.parseDouble(data.toString()));
        }

        else if (weight)
        {
            weight = false;
            weapon.setWeight(Double.parseDouble(data.toString()));
        }

        else if (name)
        {
            name = false;
            weapon.setName(data.toString());
        }

        else if (image)
        {
            image = false;
            //weapon.setItemImage(ImageUtils.loadImage("weapons", data.toString()));
        }

        else if (range)
        {
            range = false;
            weapon.setRange(Integer.parseInt(data.toString()));
        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }


}
