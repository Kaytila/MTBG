package net.ck.mtbg.backend.entities.attributes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
@ToString
public class Attributes implements ListModel<AbstractAttribute>, Serializable
{
	private final ArrayList<AbstractAttribute> attributes;

	public Attributes()
	{
		attributes = new ArrayList<>();
		getAttributes().add(new Strength());
		getAttributes().add(new Dexterity());
		getAttributes().add(new Intelligence());
		getAttributes().add(new Constitution());
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
