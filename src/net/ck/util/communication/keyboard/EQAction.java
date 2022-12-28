package net.ck.util.communication.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class EQAction extends AbstractKeyboardAction
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
    public KeyboardActionType getType()
    {
        return KeyboardActionType.EQ;
    }

    public boolean isActionimmediately()
    {
        return false;
    }

}
