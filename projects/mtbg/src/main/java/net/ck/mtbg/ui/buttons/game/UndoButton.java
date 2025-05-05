package net.ck.mtbg.ui.buttons.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.event.KeyEvent;

@Getter
@Setter
@Log4j2
public class UndoButton extends AbstractFancyButton
{

    public UndoButton(Point p)
    {
        super(p);
        this.setLabel("Debug");
        this.setMnemonic(KeyEvent.VK_U);
        this.setActionCommand(label);
    }

}
