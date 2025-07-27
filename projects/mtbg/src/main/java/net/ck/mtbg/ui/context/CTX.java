package net.ck.mtbg.ui.context;

import lombok.extern.log4j.Log4j2;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import java.awt.Component;
import java.util.EventObject;
import java.util.Objects;

/**
 * Context utils.
 */
@Log4j2
public final class CTX
{
    private static final Object CONTEXT_KEY = new Object();
    
    private CTX ()
    {
        throw new UnsupportedOperationException();
    }
    
    public static Context get (
        final Object subject )
    {
        Objects.requireNonNull(subject);
        
        final var start = componentOf(subject);
        
        if ( start == null ) {
            logger.debug("unable to get context from {}", subject);
            return null;
        }
        
        for ( var component = start; component != null; component = component.getParent() ) {
            if ( component instanceof JComponent jComponent &&
                 jComponent.getClientProperty(CONTEXT_KEY) instanceof Context context
            ) {
                return context;
            }
        }
        
        logger.debug("unable to get context from {}", subject);
        
        return null;
    }
    
    public static Context set (
        final Component component,
        final Object type )
    {
        Objects.requireNonNull(component);
        Objects.requireNonNull(type);
        
        if ( component instanceof JComponent jComponent ) {
            final var context = new ContextImpl(jComponent, type);
            jComponent.putClientProperty(CONTEXT_KEY, context);
            return context;
        }
        
        if ( component instanceof RootPaneContainer rpc ) {
            return set(component, rpc.getContentPane());
        }
        
        logger.debug("unable to set context at {}", component);
        
        return null;
    }
    
    private static Component componentOf (
        final Object subject )
    {
        if ( subject instanceof Component component ) {
            return component;
        }
        if ( subject instanceof RootPaneContainer rootPaneContainer ) {
            return rootPaneContainer.getContentPane();
        }
        if ( subject instanceof EventObject eventObject ) {
            return componentOf(eventObject.getSource());
        }
        return null;
    }
    
    private record ContextImpl(
        JComponent holder,
        Object type )
        implements Context
    { }
}
