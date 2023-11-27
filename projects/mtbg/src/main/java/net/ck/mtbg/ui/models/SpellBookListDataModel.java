package net.ck.mtbg.ui.models;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.components.SpellBookDataModelDataListener;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpellBookListDataModel implements ListModel<AbstractSpell>, Serializable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private CopyOnWriteArrayList<AbstractSpell> spells;

    public SpellBookListDataModel()
    {
        spells = new CopyOnWriteArrayList<>();
        filterSpellsByLevel();
        this.addListDataListener(new SpellBookDataModelDataListener());
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

    public void filterSpellsByLevel()
    {
        logger.debug("filtering spells begin");

        for (AbstractSpell sp : Game.getCurrent().getCurrentPlayer().getSpells())
        {
            if ((sp.getLevel() > Game.getCurrent().getCurrentPlayer().getSelectedSpellLevel()) || (sp.getLevel() < Game.getCurrent().getCurrentPlayer().getSelectedSpellLevel()))
            {
                logger.debug("not true");
                remove(sp);
            }
            else
            {
                add(sp);
                logger.debug("true");
            }
        }

        for (AbstractSpell s : spells)
        {
            logger.debug("spell:{}", s.getName());
        }
    }
}
