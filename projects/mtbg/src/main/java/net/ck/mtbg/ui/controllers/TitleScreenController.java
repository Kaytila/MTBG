package net.ck.mtbg.ui.controllers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.music.TitleMusicPlayerNoThread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Getter
@Setter
public class TitleScreenController implements WindowListener, ActionListener
{
    private static final TitleScreenController TITLE_SCREEN_CONTROLLER = new TitleScreenController();

    TitleMusicPlayerNoThread titleMusicPlayerNoThread;

    public TitleScreenController()
    {

    }

    public static TitleScreenController getCurrent()
    {
        return TITLE_SCREEN_CONTROLLER;
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        if (GameConfiguration.playMusic == true)
        {
            titleMusicPlayerNoThread = new TitleMusicPlayerNoThread();
            Path titleTrack = Paths.get("assets\\music\\STONES\\stones5.wav");
            titleMusicPlayerNoThread.playSong(titleTrack);
        }
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        if (GameConfiguration.playMusic == true)
        {
            titleMusicPlayerNoThread.pauseMusic();
        }
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        if (GameConfiguration.playMusic == true)
        {
            titleMusicPlayerNoThread.pauseMusic();
        }

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

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
}
