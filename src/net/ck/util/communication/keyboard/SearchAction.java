package net.ck.util.communication.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * so search will just search the vicinity around the player, all 9 tiles, this is done immediately, no cross-hair
 *
 */
public class SearchAction extends AbstractKeyboardAction
{
    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public Logger getLogger()
    {
        return logger;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        logger.info(getType() + " pressed");
        EventBus.getDefault().post(this);
    }

    public KeyboardActionType getType()
    {
        return KeyboardActionType.SEARCH;
    }

    public boolean isActionimmediately()
    {
        return true;
    }
}
