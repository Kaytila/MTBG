package net.ck.mtbg.ui.models;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpellBookListUIModel implements ListModel<AbstractSpell>, Serializable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private CopyOnWriteArrayList<AbstractSpell> spells;
	private int currentLevel = 0;

	public SpellBookListUIModel()
	{
		spells = new CopyOnWriteArrayList<>();
	}

	public int getCurrentLevel()
	{
		if (currentLevel == 0)
		{
			return 1;
		}
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel)
	{
		this.currentLevel = currentLevel;
	}

	@Override
	public int getSize()
	{
		return 0;
	}

	@Override
	public AbstractSpell getElementAt(int index)
	{
		return null;
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{

	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{

	}

	public CopyOnWriteArrayList<AbstractSpell> getSpells()
	{
		return spells;
	}

	public void setSpells(CopyOnWriteArrayList<AbstractSpell> spells)
	{
		this.spells = spells;
	}
}
