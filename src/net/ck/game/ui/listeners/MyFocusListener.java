package net.ck.game.ui.listeners;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.FocusEvent;

public class MyFocusListener implements java.awt.event.FocusListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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