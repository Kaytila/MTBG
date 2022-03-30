package net.ck.game.backend;

import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

/**
 * so I guess we need to have a command queue for running more than one commands after the other,
 * currently we are single command, even with multi input
 */
public class CommandQueue
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    private ArrayList<AbstractKeyboardAction> actionList;

    public ArrayList<AbstractKeyboardAction> getActionList()
    {
        return actionList;
    }

    public void setActionList(ArrayList<AbstractKeyboardAction> actionList)
    {
        this.actionList = actionList;
    }

    public boolean isEmpty()
    {
        return getActionList().isEmpty();
    }

    public void addEntry(AbstractKeyboardAction action)
    {
        getActionList().add(action);
    }

    public CommandQueue()
    {
        actionList = new ArrayList<>();
    }
}
