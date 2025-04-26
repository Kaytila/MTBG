package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

@Log4j2
@Getter
@Setter
public class GameWindowFocusListener implements FocusListener
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
