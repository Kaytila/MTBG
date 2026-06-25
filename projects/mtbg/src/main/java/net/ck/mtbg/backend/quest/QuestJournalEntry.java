package net.ck.mtbg.backend.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class QuestJournalEntry implements Serializable
{
    private int questId;
    private int stepId;
    private String text;
    private long createdAt = System.currentTimeMillis();
}
