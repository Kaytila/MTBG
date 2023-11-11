package net.ck.mtbg.backend.state;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

public class SpellManager
{
	private static Hashtable<Integer, AbstractSpell> spellList;
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public static Hashtable<Integer, AbstractSpell> getSpellList()
	{
		return spellList;
	}

	public static void setSpellList(Hashtable<Integer, AbstractSpell> spellList)
	{
		SpellManager.spellList = spellList;
	}
}
