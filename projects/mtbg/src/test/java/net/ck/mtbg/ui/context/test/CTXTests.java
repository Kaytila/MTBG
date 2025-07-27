package net.ck.mtbg.ui.context.test;

import net.ck.mtbg.ui.context.CTX;
import org.junit.jupiter.api.Test;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for {@link CTX}.
 */
final class CTXTests
{
    @Test
    void test_get ()
        throws Throwable
    {
        SwingUtilities.invokeAndWait(() -> {
            final var parent = new JComponent() { };
            final var comp = new JComponent() { };
            final var type = "test.type";
            
            parent.add(comp);
            
            final var contextOfSet = CTX.set(parent, type);
            final var contextOfGet = CTX.get(comp);
            
            assertNotNull(contextOfGet);
            assertSame(contextOfSet, contextOfGet);
            assertEquals(parent, contextOfGet.holder());
            assertEquals(type, contextOfGet.type());
        });
    }
    
    @Test
    void test_set ()
        throws Throwable
    {
        SwingUtilities.invokeAndWait(() -> {
            final var comp = new JComponent() { };
            final var type = "test.type";
            
            final var contextOfSet = CTX.set(comp, type);
            
            assertNotNull(contextOfSet);
            assertEquals(comp, contextOfSet.holder());
            assertEquals(type, contextOfSet.type());
        });
    }
}
