package net.ck.mtbg.backend.entities.skills;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Spells implements ListModel<AbstractSpell>, Serializable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private CopyOnWriteArrayList<AbstractSpell> spells;

    public Spells()
    {
        spells = new CopyOnWriteArrayList<>();
    }

    @Override
    public int getSize()
    {
        return spells.size();
    }

    @Override
    public AbstractSpell getElementAt(int index)
    {
        return spells.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {

    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {

    }

    public void add(AbstractSpell spell)
    {
        spells.add(spell);
    }

    public void remove(AbstractSpell spell)
    {
        spells.remove(spell);
    }

}
