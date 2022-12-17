package net.ck.game.music;

import net.ck.game.backend.state.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SongListMap
{

    private HashMap<GameState, ArrayList<Path>> resultMap;
    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public SongListMap()
    {
        logger.info("initialize Song Map");
    }

    public HashMap<GameState, ArrayList<Path>> getResultMap()
    {
        return resultMap;
    }

    public void setResultMap(HashMap<GameState, ArrayList<Path>> resultMap)
    {
        this.resultMap = resultMap;
    }

    public void addSong(GameState state, Path song)
    {
        if (resultMap == null)
        {
            resultMap = new HashMap<>();
        }
        if (resultMap.get(state) == null)
        {
            ArrayList<Path> list = new ArrayList<>();
            list.add(song);
            resultMap.put(state, list);
        }
        else
        {
            resultMap.get(state).add(song);
        }
    }


    public ArrayList<Path> get(GameState state)
    {
        return getResultMap().get(state);
    }
}
