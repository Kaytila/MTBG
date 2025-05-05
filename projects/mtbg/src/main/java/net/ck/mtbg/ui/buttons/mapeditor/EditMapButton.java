package net.ck.mtbg.ui.buttons.mapeditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.buttons.game.AbstractFancyButton;

import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class EditMapButton extends AbstractFancyButton
{
    public EditMapButton(ActionListener actionListener)
    {
        super();
        setLabel("Edit Map");
        this.setActionCommand(label);
        this.addActionListener(actionListener);
    }
}
