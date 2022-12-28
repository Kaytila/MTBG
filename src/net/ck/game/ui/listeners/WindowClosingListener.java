package net.ck.game.ui.listeners;

import net.ck.game.backend.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class WindowClosingListener extends WindowAdapter
{
    @Override
    public void windowClosing(WindowEvent e)
    {
        //super.windowClosing(e);
        Game.getCurrent().getController().setDialogOpened(false);
        Game.getCurrent().getController().getGridCanvas().requestFocusInWindow();
        Game.getCurrent().getController().getFrame().repaint();
    }

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
