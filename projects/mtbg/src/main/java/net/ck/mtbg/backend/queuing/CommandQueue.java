package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * so I guess we need to have a command queue for running more than one commands after the other,
 * currently we are single command, even with multi input
 */
@Log4j2
@Getter
@Setter
@ToString
public class CommandQueue implements Queue, Serializable
{
    private ArrayList<AbstractKeyboardAction> actionList;

    public CommandQueue()
    {
        actionList = new ArrayList<>();
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
        return Objects.requireNonNull(getActionList().iterator());
    }

    @Override
    public void forEach(Consumer action)
    {
        Queue.super.forEach(action);
    }

    @Override
    public Object[] toArray()
    {
        return getActionList().toArray();
    }

    @Override
    public Object[] toArray(IntFunction generator)
    {
        return Queue.super.toArray(generator);
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

    public boolean add(AbstractKeyboardAction o)
    {
        return getActionList().add(o);
    }


    public boolean remove(AbstractKeyboardAction o)
    {
        return getActionList().remove(o);
    }

    @Override
    public boolean addAll(Collection c)
    {
        return getActionList().addAll(c);
    }

    @Override
    public boolean removeIf(Predicate filter)
    {
        return Queue.super.removeIf(filter);
    }

    @Override
    public void clear()
    {
        getActionList().clear();
    }

    @Override
    public Spliterator spliterator()
    {
        return Queue.super.spliterator();
    }

    @Override
    public Stream stream()
    {
        return Queue.super.stream();
    }

    @Override
    public Stream parallelStream()
    {
        return Queue.super.parallelStream();
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
    public boolean offer(Object o)
    {
        return false;
    }

    @Override
    public AbstractKeyboardAction remove()
    {
        return getActionList().removeFirst();
    }

    @Override
    public AbstractKeyboardAction poll()
    {
        return getActionList().removeFirst();
    }

    @Override
    public AbstractKeyboardAction element()
    {
        return getActionList().getFirst();
    }

    @Override
    public AbstractKeyboardAction peek()
    {
        return getActionList().getFirst();
    }
}
