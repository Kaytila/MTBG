package net.ck.mtbg.backend.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class QuestStep implements Serializable
{
    private int id;
    private String key;
    private String title;
    private String description;
    private boolean optional;
    /**
     * Expected values initially: LOCKED, ACTIVE, COMPLETED, FAILED
     */
    private String state = "LOCKED";
}
