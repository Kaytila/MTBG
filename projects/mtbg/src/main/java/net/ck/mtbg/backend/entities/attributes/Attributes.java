package net.ck.mtbg.backend.entities.attributes;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.ArrayList;

public class Attributes implements ListModel<AbstractAttribute>, Serializable
{

	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private final ArrayList<AbstractAttribute> attributes;


	public Logger getLogger()
	{
		return logger;
	}

	public Attributes()
	{
		attributes = new ArrayList<>();
		attributes.add(new Strength());
		attributes.add(new Dexterity());
		attributes.add(new Intelligence());
		attributes.add(new Constitution());
	}

	@Override
	public int getSize()
	{
		return attributes.size();
	}

	@Override
	public AbstractAttribute getElementAt(int index)
	{
		return attributes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{

	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{

	}

	public AbstractAttribute get(AttributeTypes attributeTypes)
	{
		switch (attributeTypes)
		{
			case CONSTITUTION :
				return attributes.get(3);
			case DEXTERITY :
				return attributes.get(1);
			case INTELLIGENCE :
				return attributes.get(2);
			case STRENGTH :
				return attributes.get(0);
			default :
				return null;
		}
	}
}
