package net.ck.mtbg.ui.buttons.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class StartMusicButton extends AbstractFancyButton
{
    public StartMusicButton(Point p)
    {
        super(p);
        setLabel("Start Music");
        this.setActionCommand(label);
        this.setFocusable(false);
    }
}
