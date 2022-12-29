package net.ck.game.music;

import net.ck.game.backend.state.GameState;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class SongListMap
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private HashMap<GameState, ArrayList<Path>> resultMap;


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
