package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class DecreaseVolumeButton extends AbstractFancyButton
{
    public DecreaseVolumeButton(Point p)
    {
        super(p);
        setLabel("Leiser");
        this.setActionCommand(label);
        this.setFocusable(false);
    }
}
