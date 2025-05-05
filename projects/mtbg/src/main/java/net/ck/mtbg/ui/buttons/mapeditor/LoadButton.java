package net.ck.mtbg.ui.buttons.mapeditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.buttons.game.AbstractFancyButton;
import net.ck.mtbg.ui.controllers.GameController;

import java.awt.*;
import java.awt.event.ActionListener;

@Getter
@Setter
@Log4j2
public class LoadButton extends AbstractFancyButton
{

    public LoadButton(Point p)
    {
        super(p);
        setLabel("Load");
        this.setActionCommand(label);
        this.addActionListener(GameController.getCurrent());
    }


    public LoadButton(ActionListener actionListener)
    {
        super();
        setLabel("Load");
        this.setActionCommand(label);
        this.addActionListener(actionListener);
    }
}
