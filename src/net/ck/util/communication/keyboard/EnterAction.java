package net.ck.util.communication.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class EnterAction extends AbstractKeyboardAction
{


    /**
     *
     */
    private static final long serialVersionUID = 1L;
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
        return KeyboardActionType.ENTER;
    }

    public boolean isActionimmediately()
    {
        return true;
    }

}
