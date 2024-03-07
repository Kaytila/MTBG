package net.ck.mtbg.music;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.GameState;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@Log4j2
public class SongListMap
{
    private HashMap<GameState, ArrayList<Path>> resultMap;

    public SongListMap()
    {
        logger.info("initialize Song Map");
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
