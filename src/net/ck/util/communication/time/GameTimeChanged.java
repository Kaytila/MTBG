package net.ck.util.communication.time;

import net.ck.util.communication.graphics.ChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class GameTimeChanged extends ChangedEvent
{
    final GameTimeChangeType type;
    private final Logger logger = LogManager.getLogger(getRealClass());

    public GameTimeChanged(GameTimeChangeType typ)
    {
        super();
        this.type = typ;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }
}
