package net.ck.game.ui.listeners;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HightlightTimerActionListener implements ActionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Game.getCurrent().getController().getGridCanvas().increaseHighlightCount();
    }
}
