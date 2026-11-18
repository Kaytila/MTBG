package net.ck.mtbg.application.port;

public interface HighlightPort
{
    void start();

    void stop();

    void pause();

    void resume();

    boolean isRunning();
}