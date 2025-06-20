package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;

@Getter
@Setter
@Log4j2
@ToString
public class CastAction extends AbstractKeyboardAction
{

    private AbstractSpell spell;

    public KeyboardActionType getType()
    {
        return KeyboardActionType.CAST;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

    public int getSoundReach()
    {
        return 10;
    }

}
