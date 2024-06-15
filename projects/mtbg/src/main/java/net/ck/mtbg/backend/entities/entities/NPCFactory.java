package net.ck.mtbg.backend.entities.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.NPCManager;

@Log4j2
@Getter
@Setter
public class NPCFactory
{
    public static NPC createNPC(int ID)
    {
        return new NPC(NPCManager.getNpcList().get(ID));
    }
}
