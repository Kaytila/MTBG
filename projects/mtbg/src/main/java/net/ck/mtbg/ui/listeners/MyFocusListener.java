package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.event.FocusEvent;

@Log4j2
@Getter
@Setter
public class MyFocusListener implements java.awt.event.FocusListener
{

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
