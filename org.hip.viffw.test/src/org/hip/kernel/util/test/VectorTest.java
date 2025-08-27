package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 */
public class VectorTest {

    @Test
    public void testAdd() {
        final String[] lExpected = {"Element 1", "2nd Element"};

        final TestVector lStringSet = new TestVector();

        assertEquals(0, lStringSet.size());

        lStringSet.add(lExpected[0]);
        assertEquals(1, lStringSet.size());

        lStringSet.add(lExpected[1]);
        assertEquals(2, lStringSet.size());

        lStringSet.add(new Integer(12));
        assertEquals(2, lStringSet.size());

        assertEquals(lExpected[1], lStringSet.get(1));
        assertEquals(lExpected[0], lStringSet.get(0));
    }
}
