package net.ck.game.backend;

import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * so I guess we need to have a command queue for running more than one commands after the other,
 * currently we are single command, even with multi input
 */
public class CommandQueue implements Queue
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

    @Override
    public int size()
    {
        return 0;
    }

    public boolean isEmpty()
    {
        return getActionList().isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return false;
    }

    @Override
    public Iterator iterator()
    {
        return null;
    }

    @Override
    public Object[] toArray()
    {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a)
    {
        return new Object[0];
    }

    public void addEntry(AbstractKeyboardAction action)
    {
        getActionList().add(action);
    }

    public CommandQueue()
    {
        actionList = new ArrayList<>();
    }

    @Override
    public boolean add(Object o)
    {
        return false;
    }

    @Override
    public boolean remove(Object o)
    {
        return false;
    }

    @Override
    public boolean addAll(Collection c)
    {
        return false;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public boolean retainAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean removeAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean containsAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean offer(Object o)
    {
        return false;
    }

    @Override
    public Object remove()
    {
        return null;
    }

    @Override
    public Object poll()
    {
        return null;
    }

    @Override
    public Object element()
    {
        return null;
    }

    @Override
    public Object peek()
    {
        return null;
    }
}
