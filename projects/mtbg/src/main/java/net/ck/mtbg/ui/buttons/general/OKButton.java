package net.ck.mtbg.ui.buttons.general;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.actions.OKButtonAction;
import net.ck.mtbg.ui.buttons.game.AbstractFancyButton;

@Log4j2
@Getter
@Setter
public class OKButton extends AbstractFancyButton
{
    public OKButton()
    {
        super();
        setLabel("OK");
        this.setActionCommand(label);
        this.setAction(new OKButtonAction());
    }
}
