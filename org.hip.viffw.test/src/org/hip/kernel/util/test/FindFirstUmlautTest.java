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
        assertEquals("Ä", lUmlauts.getFoundUmlaut());
        assertEquals("AE", lUmlauts.getFoundCorresponding());
        assertEquals("B", lUmlauts.getStarting());
        assertEquals("R", lUmlauts.getEnding());

        lString = "BAERENB�BCHEN";
        lUmlauts = new FindFirstUmlaut(lString);
        assertTrue(lUmlauts.hasUmlauts());
        assertEquals(1, lUmlauts.getPosition());
        assertEquals("Ä", lUmlauts.getFoundUmlaut());
        assertEquals("AE", lUmlauts.getFoundCorresponding());
        assertEquals("B", lUmlauts.getStarting());
        assertEquals("RENB�BCHEN", lUmlauts.getEnding());

        lString = "NO UMLAUTS AT ALL";
        lUmlauts = new FindFirstUmlaut(lString);
        assertTrue(!lUmlauts.hasUmlauts());
    }
}
