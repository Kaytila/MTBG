package net.ck.mtbg.ui.actions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Log4j2
@Getter
@Setter
/**
 * try to find a generic way
 */
public class OKButtonAction extends AbstractAction
{
    public OKButtonAction()
    {
        putValue(NAME, "Close");
        putValue(SHORT_DESCRIPTION, "Closes the game");
    }

    @Override
    public boolean accept(Object sender)
    {
        return super.accept(sender);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        logger.debug("Action");
    }
}
