package net.ck.util.communication.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EscapeAction extends AbstractAction
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    private static final String KEY_STROKE_AND_KEY = "ESCAPE";
    private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke( KEY_STROKE_AND_KEY );

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public EscapeAction()
    {
        super("Escape");
    }

    /**
     *  Implement the Escape Action. First attempt to hide a popup menu.
     *  If no popups are found then dispose the window.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //  When a popup is visible the root pane of the Window will
        //  (generally) have focus

        Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        JComponent rootPane = (JComponent)component;

        //  In some cases a component added to a popup menu may have focus, but
        //  we need the root pane to check for popup menu key bindings

        if ( ! (rootPane instanceof JRootPane) )
        {
            rootPane = (JComponent)SwingUtilities.getAncestorOfClass(JRootPane.class, component);
        }

        //  Hide the popup menu when an ESCAPE key binding is found,
        //  otherwise dispose the Window

        ActionListener escapeAction = getEscapeAction( rootPane );

        if (escapeAction != null)
        {
            escapeAction.actionPerformed(null);
        }
        else
        {
            Window window = SwingUtilities.windowForComponent(component);
            window.dispose();
        }
    }

    private ActionListener getEscapeAction(JComponent rootPane)
    {
        //  Search the parent InputMap to see if a binding for the ESCAPE key
        //  exists. This binding is added when a popup menu is made visible
        //  (and removed when the popup menu is hidden).

        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (im == null) return null;

        im = im.getParent();

        if (im == null) return null;

        Object[] keys = im.keys();

        if (keys == null) return null;

        for (Object keyStroke: keys)
        {
            if (keyStroke.equals(ESCAPE_KEY_STROKE))
            {
                Object key = im.get(ESCAPE_KEY_STROKE);
                return rootPane.getActionMap().get( key );
            }
        }

        return null;
    }

    /**
     *  Convenience method for JDialogs to register the EscapeAction
     *
     *  &param dialog the JDialog the EscapeAction is registered with
     */
    public void register(JDialog dialog)
    {
        register( dialog.getRootPane() );
    }

    /**
     *  Register the EscapeAction on the specified JRootPane
     *
     *  &param rootPane the JRootPane the EscapeAction is registered with
     */
    public void register(JRootPane rootPane)
    {
        rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ESCAPE_KEY_STROKE, KEY_STROKE_AND_KEY);
        rootPane.getActionMap().put(KEY_STROKE_AND_KEY, this);
    }
}