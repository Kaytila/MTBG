package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class MouseActionListener implements ActionListener
{
    GameController win;

    public MouseActionListener(GameController gameController)
    {
        win = gameController;
    }


    @Override
    public void actionPerformed(ActionEvent evt)
    {
        logger.info("mouse pressed");
        win.setMousePressed(true);
        win.createMovement();
    }
}
