package net.ck.mtbg.backend.dialogue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class DialogueChoice implements Serializable
{
    private final List<String> requiredFlags = new ArrayList<>();
    private final List<String> blockedFlags = new ArrayList<>();
    private final Map<String, String> setFlags = new LinkedHashMap<>();
    private int id;
    private String key;
    private String text;
    private String nextNodeKey;
    private boolean oneTime;
}
