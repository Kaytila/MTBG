package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.map.MapTile;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import java.awt.Point;


public interface LifeForm
{
    void doAction(AbstractAction action);

    boolean attack(AbstractKeyboardAction action);

    void search();

    int getNumber();

    Point getMapPosition();

    void setMapPosition(Point position);

    AbstractRepresentation getAppearance();

    Attributes getAttributes();

    Point getUIPosition();

    void setUIPosition(Point calculateUIPositionFromMapOffset);

    boolean moveTo(MapTile tileByCoordinates);

    void setAgressive(boolean b);

    void setVictim(LifeForm npc);

    LifeForm getVictim();

    Point getOriginalMapPosition();
}
