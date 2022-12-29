package net.ck.game.backend.queuing;

import net.ck.util.CodeUtils;
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
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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
        return getActionList().size();
    }

    public boolean isEmpty()
    {
        return getActionList().isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return getActionList().contains(o);
    }

    @Override
    public Iterator iterator()
    {
        return getActionList().iterator();
    }

    @Override
    public Object[] toArray()
    {
        return getActionList().toArray();
    }

    @Override
    public Object[] toArray(Object[] a)
    {
        return getActionList().toArray(a);
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
        return getActionList().add((AbstractKeyboardAction) o);
    }

    @Override
    public boolean remove(Object o)
    {
        return getActionList().remove((AbstractKeyboardAction) o);
    }

    @Override
    public boolean addAll(Collection c)
    {
         return getActionList().addAll(c);
    }

    @Override
    public void clear()
    {
        getActionList().clear();
    }

    @Override
    public boolean retainAll(Collection c)
    {
        return getActionList().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c)
    {
         return getActionList().removeAll(c);
    }

    @Override
    public boolean containsAll(Collection c)
    {
        return getActionList().containsAll(c);
    }

    @Override
    public boolean offer(Object o)
    {
        return false;
    }

    @Override
    public Object remove()
    {
        return getActionList().remove(0);
    }

    @Override
    public Object poll()
    {
        return getActionList().remove(0);
    }

    @Override
    public Object element()
    {
        return  getActionList().get(0);
    }

    @Override
    public Object peek()
    {
        return getActionList().get(0);
    }
}
