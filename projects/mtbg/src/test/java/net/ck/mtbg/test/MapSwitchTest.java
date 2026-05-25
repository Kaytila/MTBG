package net.ck.mtbg.test;

import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.entities.AbstractEntity;
import net.ck.mtbg.backend.entities.entities.NPCProperty;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.gameactions.EnterAction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapSwitchTest
{
    private ArrayList<Map> originalMaps;
    private Map originalCurrentMap;

    private static Map createMap(String name, int width, int height)
    {
        Map map = new Map();
        map.setName(name);
        map.setSize(new Point(width, height));
        map.mapTiles = new MapTile[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                MapTile tile = new MapTile(x, y);
                tile.setMapPosition(new Point(x, y));
                tile.setType(TileTypes.GRASS);
                map.mapTiles[x][y] = tile;
            }
        }
        return map;
    }

    @AfterEach
    void restoreGameState()
    {
        if (originalMaps != null)
        {
            Game.getCurrent().setMaps(originalMaps);
        }
        if (originalCurrentMap != null)
        {
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    void switchMapClearsSourceTileOnOldMapAndOccupiesTargetTile()
    {
        originalMaps = Game.getCurrent().getMaps();
        originalCurrentMap = Game.getCurrent().getCurrentMap();

        Map sourceMap = createMap("source", 4, 4);
        Map targetMap = createMap("target", 4, 4);
        Game.getCurrent().setMaps(new ArrayList<>(List.of(sourceMap, targetMap)));
        Game.getCurrent().setCurrentMap(sourceMap);

        DummyEntity entity = new DummyEntity(77, new Point(1, 1));
        sourceMap.getLifeForms().add(entity);

        MapTile exitTile = sourceMap.mapTiles[1][1];
        exitTile.setLifeForm(entity);
        exitTile.setBlocked(true);
        exitTile.setTargetMap("target");
        exitTile.setTargetCoordinates(new Point(2, 2));

        MapTile staleTargetPositionOnNewMap = targetMap.mapTiles[1][1];
        staleTargetPositionOnNewMap.setBlocked(true);

        MapTile targetTile = targetMap.mapTiles[2][2];
        targetTile.setBlocked(false);

        boolean switched = entity.switchMap();

        assertTrue(switched);
        assertSame(targetMap, Game.getCurrent().getCurrentMap());
        assertEquals(new Point(2, 2), entity.getMapPosition());
        assertNull(sourceMap.mapTiles[1][1].getLifeForm());
        assertFalse(sourceMap.mapTiles[1][1].isBlocked());
        assertTrue(targetMap.mapTiles[2][2].isBlocked());
        assertSame(entity, targetMap.mapTiles[2][2].getLifeForm());
        assertTrue(targetMap.getLifeForms().contains(entity));
        assertFalse(sourceMap.getLifeForms().contains(entity));
        assertTrue(staleTargetPositionOnNewMap.isBlocked(), "Das alte Feld auf der Zielkarte darf nicht versehentlich freigeräumt werden.");
    }

    @Test
    void switchMapReturnsFalseWhenExitHasNoTarget()
    {
        originalMaps = Game.getCurrent().getMaps();
        originalCurrentMap = Game.getCurrent().getCurrentMap();

        Map sourceMap = createMap("source", 3, 3);
        Game.getCurrent().setMaps(new ArrayList<>(List.of(sourceMap)));
        Game.getCurrent().setCurrentMap(sourceMap);

        DummyEntity entity = new DummyEntity(88, new Point(1, 1));
        sourceMap.getLifeForms().add(entity);
        sourceMap.mapTiles[1][1].setLifeForm(entity);
        sourceMap.mapTiles[1][1].setBlocked(true);

        boolean switched = entity.switchMap();

        assertFalse(switched);
        assertSame(sourceMap, Game.getCurrent().getCurrentMap());
        assertEquals(new Point(1, 1), entity.getMapPosition());
        assertSame(entity, sourceMap.mapTiles[1][1].getLifeForm());
        assertTrue(sourceMap.mapTiles[1][1].isBlocked());
    }

    private static final class DummyEntity extends AbstractEntity
    {
        private final Point originalMapPosition;
        private final Hashtable<String, String> mobasks = new Hashtable<>();
        private final boolean aStatic;
        private Point originalTargetMapPosition;
        private Point targetMapPosition;
        private Schedule schedule;
        private AbstractKeyboardAction runningAction;
        private boolean ranged;
        private boolean hostile;
        private boolean patrolling;

        private DummyEntity(int id, Point position)
        {
            super();
            setId(id);
            setQueuedActions(new CommandQueue());
            setMapPosition(new Point(position.x, position.y));
            setUIPosition(new Point(0, 0));
            originalMapPosition = new Point(position.x, position.y);
            ranged = false;
            hostile = false;
            aStatic = false;
            patrolling = false;
        }

        @Override
        public boolean wieldWeapon(Weapon weapon)
        {
            setWeapon(weapon);
            return true;
        }

        @Override
        public boolean moveTo(MapTile tileByCoordinates)
        {
            return false;
        }

        @Override
        public void search()
        {
        }

        @Override
        public void doAction(AbstractAction action)
        {
        }

        @Override
        public Point getOriginalMapPosition()
        {
            return new Point(originalMapPosition.x, originalMapPosition.y);
        }

        @Override
        public Point getOriginalTargetMapPosition()
        {
            return originalTargetMapPosition;
        }

        @Override
        public void setOriginalTargetMapPosition(Point targetMapPosition)
        {
            this.originalTargetMapPosition = targetMapPosition;
        }

        @Override
        public Point getTargetMapPosition()
        {
            return targetMapPosition;
        }

        @Override
        public void setTargetMapPosition(Point targetMapPosition)
        {
            this.targetMapPosition = targetMapPosition;
        }

        @Override
        public Hashtable<String, String> getMobasks()
        {
            return mobasks;
        }

        @Override
        public Schedule getSchedule()
        {
            return schedule;
        }

        @Override
        public void setSchedule(Schedule schedule)
        {
            this.schedule = schedule;
        }

        @Override
        public AbstractKeyboardAction getRunningAction()
        {
            return runningAction;
        }

        @Override
        public void setRunningAction(AbstractKeyboardAction action)
        {
            runningAction = action;
        }

        @Override
        public boolean isRanged()
        {
            return ranged;
        }

        @Override
        public void switchWeapon(WeaponTypes ranged)
        {
            this.ranged = ranged == WeaponTypes.RANGED;
        }

        @Override
        public boolean isStatic()
        {
            return aStatic;
        }

        @Override
        public boolean isHostile()
        {
            return hostile;
        }

        @Override
        public void setHostile(boolean b)
        {
            hostile = b;
        }

        @Override
        public void evade()
        {
        }

        @Override
        public boolean isPatrolling()
        {
            return patrolling;
        }

        @Override
        public void setPatrolling(boolean patrolling)
        {
            this.patrolling = patrolling;
        }

        @Override
        public int getCurrImage()
        {
            return super.getCurrImage();
        }

        @Override
        public EnterAction lookForExit()
        {
            return null;
        }

        @Override
        public void look(MapTile tile)
        {
        }

        @Override
        public void say(String message)
        {
        }

        @Override
        public boolean isPlayer()
        {
            return false;
        }

        @Override
        public String toXML()
        {
            return "";
        }

        @Override
        public Collection<NPCProperty> getProperties()
        {
            return List.of();
        }
    }
}

