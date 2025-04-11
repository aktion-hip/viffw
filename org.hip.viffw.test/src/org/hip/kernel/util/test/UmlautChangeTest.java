package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.util.UmlautChange;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class UmlautChangeTest {
    @Test
    public void testConvert() {
        assertEquals("convert 1", "Jaemmerlich", UmlautChange.convert("Jämmerlich"));
        assertEquals("convert 2", "Aebuemaekkoele", UmlautChange.convert("Äbümäkköle")) ;
        assertEquals("convert 3", "Ueberzaehlig", UmlautChange.convert("Überzählig")) ;
    }
}
