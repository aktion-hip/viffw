package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.util.EscapedStringTokenizer;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class EscapedStringTokenizerTest {

    @Test
    void testDo() {
        String lTest = "This is a test!";
        final String[] lTestArr = {"This", "is", "a", "test!"};
        EscapedStringTokenizer lTokenizer = new EscapedStringTokenizer(lTest, " ", false);

        int i = 0;
        while (lTokenizer.hasMoreElements()) {
            assertEquals(lTestArr[i], lTokenizer.nextElement());
            i++;
        }

        lTokenizer = new EscapedStringTokenizer(lTest, " ", true);
        i = 0;
        boolean lRegularToken = true;
        while (lTokenizer.hasMoreElements()) {
            if (lRegularToken) {
                assertEquals(lTestArr[i], lTokenizer.nextElement());
                i++;
            }
            else {
                assertEquals(" ", lTokenizer.nextElement());
            }
            lRegularToken = !lRegularToken;
        }

        lTest = "This is? a test!";
        final String[] lTestArr2 = {"This", "is a", "test!"};
        lTokenizer = new EscapedStringTokenizer(lTest, " ", false, '?');
        i = 0;
        while (lTokenizer.hasMoreElements()) {
            assertEquals(lTestArr2[i], lTokenizer.nextElement());
            i++;
        }

        lTokenizer = new EscapedStringTokenizer(lTest, " ", true, '?');
        i = 0;
        lRegularToken = true;
        while (lTokenizer.hasMoreElements()) {
            if (lRegularToken) {
                assertEquals(lTestArr2[i], lTokenizer.nextElement());
                i++;
            }
            else {
                assertEquals(" ", lTokenizer.nextElement());
            }
            lRegularToken = !lRegularToken;
        }

        final String[] lTestArr3 = {"This", "is? a", "test!"};
        lTokenizer = new EscapedStringTokenizer(lTest, " ", false, '?', true);
        i = 0;
        while (lTokenizer.hasMoreElements()) {
            assertEquals(lTestArr3[i], lTokenizer.nextElement());
            i++;
        }

        try {
            lTokenizer.nextElement();
            fail("Shouldn't get here.");
        }
        catch (final java.util.NoSuchElementException exc) {
        }
    }
}
