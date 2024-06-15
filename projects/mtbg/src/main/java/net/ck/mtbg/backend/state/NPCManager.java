package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class NPCManager
{

    @Getter
    @Setter
    private static Hashtable<Integer, NPC> npcList;
}
