package net.ck.mtbg.application.port;

public interface TimerPort
{
    void start();

    void stop();

    boolean isRunning();
}
