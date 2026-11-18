package net.ck.mtbg.ui.adapters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.application.port.ScenePort;
import net.ck.mtbg.ui.mainframes.game.SceneFrame;

@Log4j2
@Getter
@Setter
public final class SceneFrameAdapter implements ScenePort
{
    private SceneFrame sceneFrame;

    @Override
    public void showCutscene()
    {
        if (sceneFrame == null)
        {
            sceneFrame = new SceneFrame();
        }

        sceneFrame.setVisible(true);
    }

    @Override
    public void closeScene()
    {
        if (sceneFrame != null)
        {
            sceneFrame.setVisible(false);
            sceneFrame.dispose();
            sceneFrame = null;
        }
    }
}