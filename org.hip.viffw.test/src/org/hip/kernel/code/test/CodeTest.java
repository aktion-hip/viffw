package org.hip.kernel.code.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class CodeTest {
    private static TestCode testCode;

    @BeforeAll
    public static void init() {
        testCode = new TestCode("2");
    }

    @Test
    public void testChangeByElementID() throws Exception {
        assertEquals("2", testCode.getElementID());
        assertEquals("ist", testCode.getLabel("de"));
        testCode.changeByElementID("4", "de");
        assertEquals("4", testCode.getElementID());
        assertEquals("Test!", testCode.getLabel("de"));
        testCode.changeByElementID("2", "de");
        assertEquals("2", testCode.getElementID());
        assertEquals("ist", testCode.getLabel("de"));
    }

    @Test
    public void testChangeByLabel() throws Exception {
        assertEquals("2", testCode.getElementID());
        assertEquals("ist", testCode.getLabel("de"));
        testCode.changeByLabel("Test!", "de");
        assertEquals("4", testCode.getElementID());
        assertEquals("Test!", testCode.getLabel("de"));
        testCode.changeByLabel("ist", "de");
        assertEquals("2", testCode.getElementID());
        assertEquals("ist", testCode.getLabel("de"));
    }

    @Test
    public void testCompare() {
        final TestCode lCode1 = new TestCode("1");
        final TestCode lCode2 = new TestCode("2");
        final TestCode lCode3 = new TestCode("3");

        //test compare
        assertEquals(1, testCode.compareTo(lCode1));
        assertEquals(0, testCode.compareTo(lCode2));
        assertEquals(-1, testCode.compareTo(lCode3));

        //test equals
        assertTrue(testCode.equals(lCode2));
        assertEquals(testCode.hashCode(), lCode2.hashCode());

        assertTrue(!testCode.equals(lCode1));
        assertTrue(testCode.hashCode() != lCode1.hashCode());
        assertTrue(!testCode.equals(lCode3));
        assertTrue(testCode.hashCode() != lCode3.hashCode());
    }

    @Test
    public void testGetElementIDs() throws Exception {
        final String[] lExpected = {"1", "2", "3", "4"};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));

        final String[] lIDs = testCode.getElementIDs();
        assertEquals(lExpected.length, lIDs.length);
        for (int i = lIDs.length-1; i>=0; i--) {
            assertTrue(lVExpected.contains(lIDs[i]));
        }
    }

    @Test
    public void testGetLabels() throws Exception {
        final String[] lExpected1 = {"Dies", "ist", "ein", "Test!"};
        final Vector<String> lVExpected1 = new Vector<String>(Arrays.asList(lExpected1));
        final String[] lExpected2 = {"This", "is", "a", "test!"};
        final Vector<String> lVExpected2 = new Vector<String>(Arrays.asList(lExpected2));

        String[] lLabels = testCode.getLabels("de");
        assertEquals(lExpected1.length, lLabels.length);
        for (int i = lLabels.length-1; i>=0; i--) {
            assertTrue(lVExpected1.contains(lLabels[i]));
        }

        lLabels = testCode.getLabels("en");
        assertEquals(lExpected2.length, lLabels.length);
        for (int i = lLabels.length-1; i>=0; i--) {
            assertTrue(lVExpected2.contains(lLabels[i]));
        }
    }
}
