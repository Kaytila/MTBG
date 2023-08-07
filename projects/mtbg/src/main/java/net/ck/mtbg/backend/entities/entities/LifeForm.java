package net.ck.mtbg.backend.entities.entities;

import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.entities.attributes.Attributes;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.GetAction;

import java.awt.*;
import java.util.Hashtable;


public interface LifeForm
{
    void doAction(AbstractAction action);

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

    CommandQueue getQueuedActions();

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

    int getCurrImage();

    void setCurrImage(int currImage);

    NPCType getType();

    void setType(NPCType type);
}
