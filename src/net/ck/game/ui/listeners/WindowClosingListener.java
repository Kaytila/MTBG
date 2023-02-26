package net.ck.game.ui.listeners;

import net.ck.game.ui.state.UIStateMachine;
import net.ck.util.CodeUtils;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowClosingListener extends WindowAdapter
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    @Override
    public void windowClosing(WindowEvent e) {
        //super.windowClosing(e);
        logger.info("closing dialog");
        UIStateMachine.setDialogOpened(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        WindowBuilder.getFrame().repaint();
    }
}
