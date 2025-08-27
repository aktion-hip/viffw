package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.util.UmlautChange;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class UmlautChangeTest {
    @Test
    void testConvert() {
        assertEquals("Jaemmerlich", UmlautChange.convert("Jämmerlich"));
        assertEquals("Aebuemaekkoele", UmlautChange.convert("Äbümäkköle"));
        assertEquals("Ueberzaehlig", UmlautChange.convert("Überzählig"));
    }
}
