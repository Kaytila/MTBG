package net.ck.mtbg.ui.controllers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.ui.mainframes.CharacterEditorFrame;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Log4j2
@Getter
@Setter
public class CharacterEditorController implements WindowListener
{
    private static final CharacterEditorController characterEditorController = new CharacterEditorController();
    CharacterEditorFrame characterEditorFrame;

    public CharacterEditorController()
    {

    }

    public static CharacterEditorController getCurrent()
    {
        return characterEditorController;
    }


    @Override
    public void windowOpened(WindowEvent e)
    {
        logger.info("CharacterEditor opened");
        UIStateMachine.setUiState(UIState.CHARACTEREDITOR);
    }

    @Override
    public void windowClosing(WindowEvent e)
    {

    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        logger.info("closing character editor frame");
        getCharacterEditorFrame().dispose();
        Game.getCurrent().stopGame();
        //RunGame.openGameUI();
    }

    @Override
    public void windowIconified(WindowEvent e)
    {

    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {

    }

    @Override
    public void windowActivated(WindowEvent e)
    {

    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {

    }
}
