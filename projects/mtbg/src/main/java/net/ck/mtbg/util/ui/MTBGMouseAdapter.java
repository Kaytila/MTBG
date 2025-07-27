package net.ck.mtbg.util.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * {@link java.awt.event.MouseAdapter} as interface with one collector method.
 */
public interface MTBGMouseAdapter
    extends MouseListener,
            MouseMotionListener,
            MouseWheelListener
{
    default void handle (
        final MouseEvent event )
    {
        // do nothing by default
    }
    
    @Override
    default void mouseClicked (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseDragged (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseEntered (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseExited (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseMoved (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mousePressed (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseReleased (
        final MouseEvent event )
    {
        handle(event);
    }
    
    @Override
    default void mouseWheelMoved (
        final MouseWheelEvent event )
    {
        handle(event);
    }
}
