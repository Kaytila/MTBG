package net.ck.mtbg.backend.entities.attributes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class Strength extends AbstractAttribute
{

	public Strength()
	{
		value = 0;
	}

	public AttributeTypes getType()
	{
		return AttributeTypes.STRENGTH;
	}

	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}
}
