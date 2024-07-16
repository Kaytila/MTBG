package net.ck.mtbg.backend.entities.attributes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Setter
@Getter
public class Constitution extends AbstractAttribute
{
    public Constitution()
    {
        value = 0;
    }

    public AttributeTypes getType()
    {
        return AttributeTypes.CONSTITUTION;
    }

    @Override
    public String toString()
    {
        return String.valueOf(getValue());
    }
}
