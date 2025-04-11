package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.AbstractHtmlPage;
import org.hip.kernel.servlet.impl.CssLink;
import org.hip.kernel.servlet.impl.CssLinkList;
import org.hip.kernel.servlet.impl.DefaultHtmlPage;
import org.hip.kernel.servlet.impl.DynamicHtmlView;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class HtmlPageTest {

    @Test
    public void testRender() throws Exception {
        final Context lContext = new TestContext();
        final TestHtmlPage lPage = new TestHtmlPage(lContext);
        final TestHtmlView lView = new TestHtmlView(lContext);
        lPage.add(lView);
        TestPrintWriter lWriter = new TestPrintWriter();

        //default page
        lPage.renderToWriter(lWriter, "");
        final String lNL = System.getProperty("line.separator"); // "" + (char)13 + (char)10;
        String lExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
                "<title>VIF</title>\n</head>\n" +
                "<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + lNL +
                "No HTML for view!" + lNL +
                "</body></html>" + lNL;
        assertEquals(lExpected, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        //page with status text
        final String lStatus = "Status: ok!";
        lPage.setStatusMessage(lStatus);
        lPage.renderToWriter(lWriter, "");

        lExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
                "<title>VIF</title>\n</head>\n" +
                "<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + lNL +
                "No HTML for view!" + lNL +
                "<b><font face='Arial' color='#0000cc' size='3'>" + lStatus + "</font></b>" + lNL +
                "</body></html>" + lNL;
        assertEquals(lExpected, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        //page with error message
        final String lError = "Fehler: Test";
        lPage.setErrorMessage(lError);
        lPage.renderToWriter(lWriter, "");

        lExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
                "<title>VIF</title>\n</head>\n" +
                "<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + lNL +
                "No HTML for view!" + lNL +
                "<b><font face='Arial' color='#cc0000' size='3'>" + lError + "</font></b>" + lNL +
                "</body></html>" + lNL;
        assertEquals(lExpected, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        //page with css links
        final CssLink lCssLink1 = new CssLink("de/css/");
        final CssLink lCssLink2 = new CssLink("de/css/", "screen");
        final CssLinkList lList = new CssLinkList();
        lList.addCssLink(lCssLink1);
        lList.addCssLink(lCssLink2);
        assertNotNull(lList);
        lPage.setCssLinks(lList);
        lPage.renderToWriter(lWriter, "");

        lExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
                "<title>VIF</title>\n" +
                "<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"./de/css/\"/>\n" +
                "<link rel=\"stylesheet\" media=\"screen\" type=\"text/css\" href=\"./de/css/\"/>\n" +
                "</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + lNL +
                "No HTML for view!" + lNL +
                "</body></html>" + lNL;
        assertEquals(lExpected, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        //page with onLoad attributes
        final String lOnLoad = "mouseOver()";
        lPage.setOnLoad(lOnLoad);
        lPage.renderToWriter(lWriter, "");

        lExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
                "<title>VIF</title>\n" +
                "<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"./de/css/\"/>\n" +
                "<link rel=\"stylesheet\" media=\"screen\" type=\"text/css\" href=\"./de/css/\"/>\n" +
                "</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"" + lOnLoad + "\">" + lNL +
                "No HTML for view!" + lNL +
                "</body></html>" + lNL;
        assertEquals(lExpected, lWriter.getStreamedText());
    }

    @Test
    public void testEquals() throws Exception {
        AbstractHtmlPage lPage1 = new DefaultHtmlPage();
        AbstractHtmlPage lPage2 = new DefaultHtmlPage();
        final AbstractHtmlPage lPage3 = new TestPageableHtmlPage(new TestContext());

        assertFalse(lPage1.equals(this));
        assertTrue(lPage1.equals(lPage2));
        assertFalse(lPage1.equals(lPage3));

        lPage1.add(new DynamicHtmlView(""));
        assertFalse(lPage1.equals(lPage2));
        lPage2.add(new DynamicHtmlView(""));
        assertTrue(lPage1.equals(lPage2));

        lPage1 = new DefaultHtmlPage();
        lPage2 = new DefaultHtmlPage();

        final HtmlView lView1 = new DynamicHtmlView("<p>Test 1</p>");
        final HtmlView lView2 = new TestHtmlPage();
        final HtmlView lView3 = new DynamicHtmlView("<p>Test 1</p>");

        lPage1.add(lView1);
        lPage2.add(lView3);
        assertTrue(lPage1.equals(lPage2));

        lPage1.add(lView3);
        assertFalse(lPage1.equals(lPage2));

        lPage2.add(lView1);
        assertTrue(lPage1.equals(lPage2));

        lPage1.add(lView2);
        assertFalse(lPage1.equals(lPage2));

        lPage1 = new DefaultHtmlPage();
        lPage2 = new DefaultHtmlPage();
        lPage1.add(new DynamicHtmlView("<p>Test 1</p>"));
        lPage2.add(new DynamicHtmlView("<p>Test 1</p>"));
        lPage1.add(new DynamicHtmlView("<p>Test 2</p>"));
        lPage2.add(new DynamicHtmlView("<p>Test 3</p>"));
        assertFalse(lPage1.equals(lPage2));
    }
}
