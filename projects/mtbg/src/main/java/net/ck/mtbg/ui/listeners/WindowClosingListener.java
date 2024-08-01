package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.ui.WindowBuilder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Getter
@Setter
@Log4j2
@ToString
public class WindowClosingListener extends WindowAdapter
{


    @Override
    public void windowClosing(WindowEvent e)
    {
        //super.windowClosing(e);
        logger.info("closing dialog");
        UIStateMachine.setDialogOpened(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        WindowBuilder.getFrame().repaint();
    }
}
