package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class ViewTest {

    @Test
    public void testDo() {
        final Context lContext = new TestContext();
        final TestView lView = new TestView(lContext);
        assertEquals("de", lView.getLanguage());

        lContext.setLanguage("en");
        assertEquals(VSys.dftLanguage, lView.getLanguage());
    }

    @Test
    public void testLocale() {
        TestView lView = new TestView();
        assertEquals("en_US", lView.getLocale().toString());

        final Context lContext = new TestContext();
        lView = new TestView(lContext);
        assertEquals("de_CH", lView.getLocale().toString());

        lContext.setLanguage("en");
        lView = new TestView(lContext);
        assertEquals("en_CH", lView.getLocale().toString());
    }
}
