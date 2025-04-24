package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class CancelButton extends AbstractFancyButton
{
    public CancelButton()
    {
        super();
        setLabel("Cancel");
        this.setActionCommand(label);
    }
}
