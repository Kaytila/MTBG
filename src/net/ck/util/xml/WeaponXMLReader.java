package net.ck.util.xml;

import java.util.Hashtable;

import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponDamageTypes;
import net.ck.game.items.WeaponTypes;
import net.ck.util.ImageUtils;

public class WeaponXMLReader extends DefaultHandler
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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
	
	public Hashtable<Integer, Weapon> getWeaponList()
	{
		return weaponList;
	}

	public void setWeaponList(Hashtable<Integer, Weapon> weaponList)
	{
		this.weaponList = weaponList;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Logger getLogger()
	{
		return logger;
	}

	

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
			Range<Integer> range =  Range.between(Integer.valueOf(damages[0]), Integer.valueOf(damages[1]));
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
		
		else if(name)
		{
			name = false;
			weapon.setName(data.toString());
		}
		
		else if(image)
		{
			image = false;
			weapon.setItemImage(ImageUtils.loadImage("weapons", data.toString()));
		}
		
	}

	@Override
	public void characters(char ch[], int start, int length)
	{
		data.append(new String(ch, start, length));
	}
	
	
}
