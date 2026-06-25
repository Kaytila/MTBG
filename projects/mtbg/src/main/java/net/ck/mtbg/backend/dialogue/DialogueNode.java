package net.ck.mtbg.backend.dialogue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class DialogueNode implements Serializable
{
    private final List<DialogueChoice> choices = new ArrayList<>();
    private int id;
    private String key;
    private String speaker;
    private String text;
    private boolean repeatable = true;
    private String fallbackNodeKey;

    public void addChoice(DialogueChoice choice)
    {
        if (choice != null)
        {
            choices.add(choice);
        }
    }
}
