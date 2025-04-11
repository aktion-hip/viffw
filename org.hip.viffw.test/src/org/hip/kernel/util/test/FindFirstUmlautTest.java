package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.util.FindFirstUmlaut;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class FindFirstUmlautTest {

    @Test
    public void testHasUmlauts() {
        String lString = "BÄR";
        FindFirstUmlaut lUmlauts = new FindFirstUmlaut(lString);
        assertTrue(lUmlauts.hasUmlauts());
        assertEquals(1, lUmlauts.getPosition());
        assertEquals("Found Umlaut: ", "Ä", lUmlauts.getFoundUmlaut());
        assertEquals("Found Corresponding: ", "AE", lUmlauts.getFoundCorresponding());
        assertEquals("Starting: ", "B", lUmlauts.getStarting());
        assertEquals("Endifn: ", "R", lUmlauts.getEnding());

        lString = "BAERENB�BCHEN";
        lUmlauts = new FindFirstUmlaut(lString);
        assertTrue(lUmlauts.hasUmlauts());
        assertEquals(1, lUmlauts.getPosition());
        assertEquals("Found Umlaut: ", "Ä", lUmlauts.getFoundUmlaut());
        assertEquals("Found Corresponding: ", "AE", lUmlauts.getFoundCorresponding());
        assertEquals("Starting: ", "B", lUmlauts.getStarting());
        assertEquals("Endifn: ", "RENB�BCHEN", lUmlauts.getEnding());

        lString = "NO UMLAUTS AT ALL";
        lUmlauts = new FindFirstUmlaut(lString);
        assertTrue(!lUmlauts.hasUmlauts());
    }
}
