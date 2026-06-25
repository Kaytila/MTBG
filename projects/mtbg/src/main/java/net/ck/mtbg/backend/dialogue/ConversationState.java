package net.ck.mtbg.backend.dialogue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class ConversationState implements Serializable
{
    private final Set<String> visitedNodeKeys = new LinkedHashSet<>();
    private final Set<String> chosenChoiceKeys = new LinkedHashSet<>();
    private int npcId;
    private String dialogueKey;
    private String currentNodeKey;
    private boolean finished;
    private long lastUpdatedAt = System.currentTimeMillis();
}
