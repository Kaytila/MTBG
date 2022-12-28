package net.ck.game.ui.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.FocusEvent;
import java.util.Objects;

public class MyFocusListener implements java.awt.event.FocusListener
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        //logger.info("focus gained: {}", e.getComponent());
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        //logger.info("focus lost: {}", e.getComponent());
    }
}
