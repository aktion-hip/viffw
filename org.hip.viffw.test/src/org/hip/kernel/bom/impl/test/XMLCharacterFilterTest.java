package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.impl.XMLCharacterFilter;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class XMLCharacterFilterTest {

    @Test
    public void testFilter() {

        final String[][] lChars = new String[4][2];

        //initialize the field with the characters to replace
        lChars[0][0] = "<";
        lChars[0][1] = "&lt;";
        lChars[1][0] = "&";
        lChars[1][1] = "&amp;";
        lChars[2][0] = "\"";
        lChars[2][1] = "&quot;";
        lChars[3][0] = ">";
        lChars[3][1] = "&gt;";

        final XMLCharacterFilter lFilter = new XMLCharacterFilter(lChars, false);

        final String lTest = "Dies ist ein <Test> des \"&Filters\"";
        final String lExpected = "Dies ist ein &lt;Test&gt; des &quot;&amp;Filters&quot;";
        assertEquals("filter", lExpected, lFilter.filter(lTest));
    }
}
