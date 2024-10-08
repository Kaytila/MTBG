package net.ck.mtbg.util.ui;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.dialogs.DialogFactory;
import net.ck.mtbg.ui.listeners.Controller;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.utils.CursorUtils;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.NPCUtils;

import java.awt.*;
import java.awt.event.WindowEvent;

@Log4j2
public class ControllerDelegator
{
    public static void handleKeyboardActionPUSH(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }

            //logger.info("select tile is active, dont do anything");
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);

            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionGET(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            //logger.info("select tile is active, dont do anything");
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            //TODO this needs to be done in every keyboard command if selectTile is active
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            controller.getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    /**
     * takes care of look action, both initial pressing of L and pressing L a second time
     *
     * @param controller the controller class
     * @param action     keyboard action
     */
    public static void handleKeyBoardActionLOOK(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }
            //logger.info("select tile is active, dont do anything");
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            controller.getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());

        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionTALK(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }

            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(false);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Talk", false, controller.getCurrentAction(), null, tile.getLifeForm());
            logger.info("talk: {}", "");
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionMOVE(Controller controller, AbstractKeyboardAction action)
    {
        logger.info("move");
        if (UIStateMachine.isMouseOutsideOfGrid() == true)
        {
            CursorUtils.centerCursorOnPlayer();
        }

        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("select tile is active, dont do anything");
        }
        else
        {
            action.setHaveNPCAction(false);
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            //TODO why is get currect Action null?
            //TODO action framework needs to be cleaned up
            action.setGetWhere(new Point(tile.getX(), tile.getY()));
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionATTACK(Controller controller, AbstractKeyboardAction action)
    {
        //second time a is pressed
        //action is already set
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }
            logger.info("select tile is active, allow attack");
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            controller.getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            //logger.info("attack");
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            TimerManager.getIdleTimer().stop();
            //ranged
            if (Game.getCurrent().getCurrentPlayer().getWeapon() != null)
            {
                if (Game.getCurrent().getCurrentPlayer().getWeapon().getType().equals(WeaponTypes.RANGED))
                {
                    action.setOldMousePosition(MouseInfo.getPointerInfo().getLocation());
                    Point relativePoint = WindowBuilder.getGridCanvas().getLocationOnScreen();
                    CursorUtils.moveMouse(new Point(NPCUtils.calculatePlayerPosition().x + relativePoint.x, NPCUtils.calculatePlayerPosition().y + relativePoint.y));
                    action.setSourceCoordinates(NPCUtils.calculatePlayerPosition());
                    CursorUtils.moveMouse(action.getOldMousePosition());

                    UIStateMachine.setSelectTile(true);
                    UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                    WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    controller.setCurrentAction(action);
                }
                //melee
                else
                {
                    logger.info("real melee");
                    CursorUtils.centerCursorOnPlayer();
                    UIStateMachine.setSelectTile(true);
                    UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                    WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

                    controller.setCurrentAction(action);
                }
            }
            //no weapon, also use melee for now
            else
            {
                logger.info("unarmed melee");
                CursorUtils.centerCursorOnPlayer();
                UIStateMachine.setSelectTile(true);
                //CursorUtils.limitMouseMovementToRange(1);
                controller.setCurrentAction(action);
            }
        }
    }

    public static void handleKeyBoardActionSEARCH(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {

        }
        else
        {
            action.setHaveNPCAction(false);
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionESC(Controller controller, AbstractKeyboardAction action)
    {
        //logger.info("ESC Pressed");
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("selection is true");
            UIStateMachine.setSelectTile(false);
            controller.setMovementForSelectTile(false);
            TimerManager.getIdleTimer().start();
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(null);
        }
        else
        {
            logger.info("stopping game");
            WindowBuilder.getFrame().dispatchEvent(new WindowEvent(WindowBuilder.getFrame(), WindowEvent.WINDOW_CLOSING));
            Game.getCurrent().stopGame();
        }
    }

    public static void handleKeyBoardActionENTER(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionENTER - select tile is active, dont do anything");
        }
        else
        {
            action.setHaveNPCAction(true);
        }
    }

    public static void handleKeyBoardActionDROP(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionDROP - select tile is active, dont do anything");
        }
        else
        {
            TimerManager.getIdleTimer().stop();
            action.setHaveNPCAction(false);
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Inventory", false, action, null, null);
        }
    }

    public static void handleKeyBoardActionSPELLBOOK(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionSPELLBOOK - select tile is active, dont do anything");
        }
        else
        {
            action.setHaveNPCAction(false);
            logger.info("spellbook as separate event type, lets not add this to the action queue");
            TimerManager.getIdleTimer().stop();
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Spellbook", true, action, null, null);
            logger.info("spellbook dialog done");

            if (WindowBuilder.getController().getCurrentSpellInHand() != null)
            {
                logger.debug(() -> "spell is in hand, do something with it");
                logger.debug("test");
                UIStateMachine.setSelectTile(true);
                UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                controller.setCurrentAction(action);
            }
            else
            {
                logger.debug("@Simon - current spell in hand is null! that is not possible");
            }
        }
    }

    public static void handleKeyBoardActionCAST(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionCAST - select tile is active, dont do anything");
        }
        else
        {
            if (controller.getCurrentSpellInHand() == null)
            {
                logger.debug("no spell ready");
                TimerManager.getIdleTimer().stop();
                TimerManager.getIdleTimer().start();
            }
            else
            {
                action.setHaveNPCAction(false);

                TimerManager.getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                controller.setCurrentAction(action);
            }
        }
    }


    public static void handleKeyBoardActionMOVEMENT(Controller controller, AbstractKeyboardAction action)
    {

    }

    public static void handleKeyBoardActionZSTATS(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionZSTATS - select tile is active, dont do anything");
        }
        else
        {
            action.setHaveNPCAction(false);

            logger.info("zstats as separate event type, lets not add this to the action queue");
            TimerManager.getIdleTimer().stop();
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Z-Stats", false, action, null, null);
            logger.info("stats: {}", Game.getCurrent().getCurrentPlayer().getAttributes());
        }
    }

    public static void handleKeyBoardActionINVENTORY(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionINVENTORY - select tile is active, dont do anything");
        }
        else
        {
            TimerManager.getIdleTimer().stop();
            action.setHaveNPCAction(false);
            logger.info("inventory as separate event type, lets not add this to the action queue");

            DialogFactory.createDialog(WindowBuilder.getFrame(), "Inventory", false, action, null, null);
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        }
    }

    public static void handleKeyBoardActionYANK(Controller controller, AbstractKeyboardAction action)
    {
        TimerManager.getIdleTimer().stop();
        //CursorUtils.centerCursorOnPlayer();
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.debug("do the yank");
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }

            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    public static void handleKeyBoardActionEQ(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionEQ - select tile is active, dont do anything");
        }
        else
        {
            TimerManager.getIdleTimer().stop();
            action.setHaveNPCAction(false);
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Equipment", false, action, null, null);
        }
    }

    public static void handleKeyBoardActionSPACE(Controller controller, AbstractKeyboardAction action)
    {
        action.setHaveNPCAction(true);
    }

    public static void handleKeyBoardActionOPTIONS(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionOPTIONS - select tile is active, dont do anything");
        }
        else
        {
            TimerManager.getIdleTimer().stop();
            action.setHaveNPCAction(false);
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Options", false, null, null, null);
        }
    }

    public static void handleMouseReleasedActionPUSH(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
    }

    public static void handleMouseReleasedActionYank(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
    }

    public static void handleMouseReleasedActionGET(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
    }

    public static void handleMouseReleasedActionLOOK(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
    }

    public static void handleMouseReleasedActionDROP(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        controller.getCurrentAction().setAffectedItem(controller.getCurrentItemInHand());
    }


    public static void handleMouseReleasedActionTALK(Controller controller, MapTile tile)
    {
        boolean found = false;
        controller.getCurrentAction().setHaveNPCAction(false);
        LifeForm npc = null;
        if (tile.getLifeForm() != null)
        {
            found = true;
            npc = tile.getLifeForm();
        }

        if (found)
        {
            logger.info("found the npc");
            UIStateMachine.setSelectTile(false);

            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            if (UIStateMachine.isDialogOpened() == true)
            {
                return;
            }
            else
            {
                DialogFactory.createDialog(WindowBuilder.getFrame(), "Talk", false, controller.getCurrentAction(), null, npc);
                logger.info("talk: {}", "");
                TimerManager.getIdleTimer().stop();
            }
        }
        else
        {
            logger.info("no NPC here!");
            controller.setCurrentAction(new AbstractKeyboardAction());
        }
    }

    public static void handleMouseReleasedActionMOVE(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(false);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
    }

    public static void handleMouseReleasedActionATTACK(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
        controller.getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
    }

    public static void handleMouseReleasedActionSPELLBOOK(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        controller.getCurrentAction().setCurrentSpell(controller.getCurrentSpellInHand());
        logger.debug("calling click event");
    }

    public static void handleMouseReleasedActionCAST(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        controller.getCurrentAction().setCurrentSpell(controller.getCurrentSpellInHand());
        logger.debug("calling click event");
    }

    public static void handleKeyBoardActionSKILLTREE(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionSKILLTREE - select tile is active, dont do anything");
        }
        else
        {
            action.setHaveNPCAction(false);
            logger.info("skilltree as separate event type, lets not add this to the action queue");
            TimerManager.getIdleTimer().stop();
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Skilltree", true, action, null, null);
            logger.info("skilltree dialog done");

            if (WindowBuilder.getController().getCurrentSpellInHand() != null)
            {
                logger.debug(() -> "spell is in hand, do something with it");
                logger.debug("test");
                UIStateMachine.setSelectTile(true);
                UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);

                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                controller.setCurrentAction(action);
            }
            else
            {
                logger.debug("no skill selected, but then again, should it?");
            }
        }

    }

    public static void handleKeyBoardActionOPEN(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }
            //logger.info("select tile is active, dont do anything");
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);
            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            controller.getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
            action.setMapTile(tile);
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }
    }

    public static void handleMouseReleasedActionOPEN(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        controller.getCurrentAction().setMapTile(tile);
        logger.debug("calling click event");
    }

    public static void handleKeyBoardActionMAP(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            logger.info("handleKeyBoardActionMAP - select tile is active, dont do anything");
        }
        else
        {
            TimerManager.getIdleTimer().stop();
            action.setHaveNPCAction(false);
            DialogFactory.createDialog(WindowBuilder.getFrame(), "Map", false, null, null, null);
        }
    }

    public static void handleKeyBoardActionJIMMY(Controller controller, AbstractKeyboardAction action)
    {
        if (UIStateMachine.isSelectTile() == true)
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
            if (tile.isHidden())
            {
                logger.debug("hidden tile");
                return;
            }

            //logger.info("select tile is active, dont do anything");
            UIStateMachine.setSelectTile(false);
            controller.getCurrentAction().setHaveNPCAction(true);

            controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
            TimerManager.getIdleTimer().stop();
            controller.runActions(controller.getCurrentAction());
        }
        else
        {
            if (UIStateMachine.isMouseOutsideOfGrid() == true)
            {
                CursorUtils.centerCursorOnPlayer();
            }
            action.setHaveNPCAction(false);
            //logger.info("get");
            TimerManager.getIdleTimer().stop();
            UIStateMachine.setSelectTile(true);
            UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
            WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10, GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20);
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            controller.setCurrentAction(action);
        }

    }

    public static void handleMouseReleasedActionJIMMY(Controller controller, MapTile tile)
    {
        UIStateMachine.setSelectTile(false);
        controller.getCurrentAction().setHaveNPCAction(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        controller.getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
        controller.getCurrentAction().setMapTile(tile);
    }
}
