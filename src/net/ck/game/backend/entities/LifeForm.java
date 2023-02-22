package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.queuing.Schedule;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.GetAction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;


public interface LifeForm
{
    void doAction(AbstractAction action);

    boolean attack(AbstractKeyboardAction action);

    boolean attack(MapTile tile);

    int getHealth();

    void setHealth(int i);

    void increaseHealth(int i);

    void decreaseHealth(int i);

    int getArmorClass();


     void setArmorClass(int armorClass);
     Weapon getWeapon();

    boolean wieldWeapon(Weapon weapon);

     void setWeapon(Weapon weapon);

    void search();

    int getId();

    Point getMapPosition();

    void setMapPosition(Point position);


    Attributes getAttributes();

    Point getUIPosition();

    void setUIPosition(Point calculateUIPositionFromMapOffset);

    boolean moveTo(MapTile tileByCoordinates);

    void setHostile(boolean b);

    void setVictim(LifeForm npc);

    LifeForm getVictim();

    Point getOriginalMapPosition();

    LifeFormState getState();

    void setState(LifeFormState state);

    boolean isRanged();

    void switchWeapon(WeaponTypes ranged);

    GetAction lookAroundForItems();

    Queue<Object> getQueuedActions();

    boolean isStatic();

    boolean isHostile();

    void evade();

    Point getOriginalTargetMapPosition();

    void setOriginalTargetMapPosition(Point targetMapPosition);

    Point getTargetMapPosition();

    void setTargetMapPosition(Point targetMapPosition);

    boolean isPatrolling();


    void setPatrolling(boolean patrolling);

    Hashtable<String, String> getMobasks();

    void setSchedule(Schedule schedule);

    Schedule getSchedule();

    void setRunningAction(AbstractKeyboardAction action);

    AbstractKeyboardAction getRunningAction();

    void setCurrentImage(BufferedImage hitImage);

    BufferedImage getCurrentImage();

    ArrayList<BufferedImage> getAnimationImageList();
}
