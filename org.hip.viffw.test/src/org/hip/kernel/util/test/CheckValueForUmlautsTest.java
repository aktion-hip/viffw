package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.hip.kernel.util.CheckValueForUmlauts;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class CheckValueForUmlautsTest {

    @Test
    void testElements() {
        String lProcessed = "";
        String lOriginal = "";
        String lExpected = "";
        CheckValueForUmlauts lCheckValue = new CheckValueForUmlauts(lOriginal);
        Iterator<String> lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals(lExpected, lProcessed);

        lOriginal = "Bärner";
        lExpected = "BÄRNERBAERNER";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals(lExpected, lProcessed);

        lOriginal = "Baerner";
        lExpected = "BÄRNERBAERNER";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals(lExpected, lProcessed);

        lOriginal = "Bärnerbuecher";
        lExpected = "BÄRNERBÜCHER BAERNERBÜCHER BÄRNERBUECHER BAERNERBUECHER ";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next() + " ";
        }
        assertEquals(lExpected, lProcessed);

        lOriginal = "no umlauts at all";
        lExpected = "NO UMLAUTS AT ALL";
        lCheckValue = new CheckValueForUmlauts(lOriginal);
        lProcessed = "";
        lSearchStrings = lCheckValue.elements();
        while (lSearchStrings.hasNext()) {
            lProcessed += lSearchStrings.next();
        }
        assertEquals(lExpected, lProcessed);
    }
}
