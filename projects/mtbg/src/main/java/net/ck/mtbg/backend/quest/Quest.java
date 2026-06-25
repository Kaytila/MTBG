package net.ck.mtbg.backend.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Quest implements Serializable
{
    private final List<QuestStep> steps = new ArrayList<>();
    private int id;
    private String key;
    private String title;
    private String description;
    /**
     * Expected values initially: HIDDEN, AVAILABLE, ACTIVE, COMPLETED, FAILED
     */
    private String state = "HIDDEN";

    public void addStep(QuestStep step)
    {
        if (step != null)
        {
            steps.add(step);
        }
    }
}
