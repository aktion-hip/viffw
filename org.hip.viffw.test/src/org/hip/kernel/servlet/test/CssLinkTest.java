package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.servlet.impl.CssLink;
import org.hip.kernel.servlet.impl.CssLinkList;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class CssLinkTest {

    @Test
    public void testCreate() {
        final CssLink lCssLink = new CssLink("de/css/");
        assertNotNull(lCssLink);
        String lExpected = "<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"./de/css/\"/>";
        assertEquals(lExpected, lCssLink.toString());

        final CssLink lCssLink2 = new CssLink("de/css/", "screen");
        assertNotNull(lCssLink2);
        lExpected = "<link rel=\"stylesheet\" media=\"screen\" type=\"text/css\" href=\"./de/css/\"/>";
        assertEquals(lExpected, lCssLink2.toString());
    }

    @Test
    public void testEquals() {
        final CssLink lCssLink1 = new CssLink("de/css/");
        final CssLink lCssLink2 = new CssLink("de/css/", "screen");
        final CssLink lCssLink3 = new CssLink("de/css/");

        assertTrue(lCssLink1.equals(lCssLink3));
        assertEquals(lCssLink1.hashCode(), lCssLink3.hashCode());
        assertTrue(!lCssLink1.equals(lCssLink2));
        assertTrue(lCssLink1.hashCode() != lCssLink2.hashCode());
    }

    @Test
    public void testLinks() {
        final CssLink lCssLink1 = new CssLink("de/css/");
        final CssLink lCssLink2 = new CssLink("de/css/", "screen");
        final CssLink lCssLink3 = new CssLink("de/css/");

        String lExpected = "";

        //create list
        final CssLinkList lList = new CssLinkList();
        assertEquals(lExpected, lList.toString());

        //add first CssLink
        lList.addCssLink(lCssLink1);
        lExpected = "<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"./de/css/\"/>\n";
        assertEquals(lExpected, lList.toString());

        //add second CssLink
        lList.addCssLink(lCssLink2);
        lExpected = "<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"./de/css/\"/>\n" +
                "<link rel=\"stylesheet\" media=\"screen\" type=\"text/css\" href=\"./de/css/\"/>\n";
        assertEquals(lExpected, lList.toString());

        //add third CssLink which is equal to first, therefore, nothing is added
        lList.addCssLink(lCssLink3);
        assertEquals(lExpected, lList.toString());

        //add first CssLink again, therefore, nothing is added
        lList.addCssLink(lCssLink1);
        assertEquals(lExpected, lList.toString());
    }
}
