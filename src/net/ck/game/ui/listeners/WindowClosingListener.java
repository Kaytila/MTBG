package net.ck.game.ui.listeners;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowClosingListener extends WindowAdapter
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    @Override
    public void windowClosing(WindowEvent e)
    {
        //super.windowClosing(e);
        logger.info("closing dialog");
        Game.getCurrent().getController().setDialogOpened(false);
        Game.getCurrent().getController().getGridCanvas().requestFocusInWindow();
        Game.getCurrent().getController().getFrame().repaint();
    }
}
