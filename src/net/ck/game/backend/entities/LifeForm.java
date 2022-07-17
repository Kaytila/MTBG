package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;

import java.awt.*;

public interface LifeForm
{
    default void doAction(AbstractAction action)
    {
    }

    default void setMapPosition(Point position)
    {
    }

    default boolean attack(AbstractKeyboardAction action)
    {
        return false;
    }


    default void search()
    {

    }



}
