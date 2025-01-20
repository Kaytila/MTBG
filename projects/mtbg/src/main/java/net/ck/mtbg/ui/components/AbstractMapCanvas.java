package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public class AbstractMapCanvas extends JComponent
{

    public int getCurrentBackgroundImage()
    {
        return 0;
    }
}
