package net.ck.mtbg.application.port;

public interface GameEventPublisher
{
    void publish(Object event);
}