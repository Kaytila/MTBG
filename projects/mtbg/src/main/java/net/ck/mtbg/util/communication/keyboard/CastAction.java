package net.ck.mtbg.util.communication.keyboard;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CastAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private AbstractSpell spell;

    public KeyboardActionType getType() {
        return KeyboardActionType.CAST;
    }

    public boolean isActionimmediately() {
        return false;
    }

    public void setSpell(AbstractSpell selectedValue) {
        this.spell = selectedValue;
    }

    public AbstractSpell getSpell() {
        return spell;
    }
}
