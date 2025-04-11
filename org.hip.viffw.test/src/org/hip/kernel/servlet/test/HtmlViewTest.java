package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.DynamicHtmlView;
import org.junit.jupiter.api.Test;

/**
 * This test needs an html-File VIFTest.html in the subdirectory de (according to vif.properties)
 * ./de/VIFTest.html
 *
 * @author: Benno Luthiger
 */
public class HtmlViewTest {

    @Test
    public void testDo() throws Exception {
        final Context lContext = new TestContext();
        final TestHtmlView lView = new TestHtmlView(lContext);
        TestPrintWriter lWriter = new TestPrintWriter();
        lView.renderToWriter(lWriter, "");

        final char lCR = (char)13;
        final char lLF = (char)10;
        String lExpected = "No HTML for view!" + lCR + lLF;
        assertEquals(lExpected, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lView.setHTMLString(lView.getReadHTML());
        lView.renderToWriter(lWriter, "");
        lExpected = FileReadWriteUtility.readFile(new File("de/VIFTest.html"));
        assertEquals(lExpected.substring(0, lExpected.length() - 2), lWriter.getStreamedText());
    }

    @Test
    public void testReadHTML() {
        final Context lContext = new TestContext();
        final TestHtmlView lView = new TestHtmlView(lContext);
        final String lExpected = FileReadWriteUtility.readFile(new File("de/VIFTest.html"));
        assertEquals("html-file", lExpected.substring(0, lExpected.length()-2), lView.getReadHTML());
    }

    @Test
    public void testEquals() throws Exception {
        HtmlView lView1 = new DynamicHtmlView("");
        final HtmlView lView2 = new TestHtmlPage();
        HtmlView lView3 = new DynamicHtmlView("");

        assertFalse(lView1.equals(this));
        assertFalse(lView1.equals(lView2));
        assertFalse(lView2.equals(lView1));
        assertTrue(lView1.equals(lView3));
        assertFalse(lView2.equals(lView3));

        lView1 = new DynamicHtmlView("<p>Test1</p>");
        lView3 = new DynamicHtmlView("<p>Test2</p>");

        assertFalse(lView1.equals(lView3));

        lView3 = new DynamicHtmlView("<p>Test1</p>");
        assertTrue(lView1.equals(lView3));
    }

}
