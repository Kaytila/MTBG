package net.ck.mtbg.ui.context;

import javax.swing.JComponent;

/**
 * Context with a type and holder.
 */
public interface Context
{
    JComponent holder ();
    
    Object type ();
}
