package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.ui.WindowBuilder;

import java.awt.*;
import java.awt.event.ActionListener;

@Getter
@Setter
@Log4j2
public class SaveButton extends AbstractFancyButton
{
    public SaveButton(Point p)
    {
        super();
        setLabel("Save");
        this.setActionCommand(label);
        this.addActionListener(WindowBuilder.getGameController());
        this.setVisible(true);
    }

    public SaveButton(ActionListener actionListener)
    {
        super();
        setLabel("Save");
        this.setActionCommand(label);
        this.addActionListener(actionListener);
        this.setVisible(true);
    }
}
