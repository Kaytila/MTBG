package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class SpellManager
{
	@Getter
	@Setter
	private static Hashtable<Integer, AbstractSpell> spellList;
}
