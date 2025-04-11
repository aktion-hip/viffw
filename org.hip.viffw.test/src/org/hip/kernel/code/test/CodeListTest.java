package org.hip.kernel.code.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Vector;

import org.hip.kernel.code.AbstractCode;
import org.hip.kernel.code.CodeList;
import org.hip.kernel.code.CodeListFactory;
import org.hip.kernel.code.CodeNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class CodeListTest {
    private static CodeList codeList;

    @BeforeAll
    public static void init() {
        codeList = new CodeListFactory().createCodeList("CodeTest", "de");
    }

    @Test
    public void testDo() {
        final String[] lExpectedIDs = {"1", "4", "3", "2"};
        final String lExpIDs = " 1234";
        final String[] lExpectedLabels = {"Dies", "Test!", "ein", "ist"};
        final String lExpLabels = " Dies ist ein Test!";

        //test returned String-Arrays
        int i = 0;
        String[] lActual = codeList.getElementIDs();
        assertEquals(lExpectedIDs.length, lActual.length);
        for (i = 0; i<lActual.length; i++) {
            assertEquals("sorted ID " + i, lExpectedIDs[i], lActual[i]);
        }

        lActual = codeList.getElementIDsUnsorted();
        assertEquals(lExpectedIDs.length, lActual.length);
        for (i = 0; i<lActual.length; i++) {
            assertTrue(lExpIDs.indexOf(lActual[i]) > 0);
        }

        lActual = codeList.getLabels();
        assertEquals(lExpectedLabels.length, lActual.length);
        for (i = 0; i<lActual.length; i++) {
            assertEquals("sorted label " + i, lExpectedLabels[i], lActual[i]);
        }

        lActual = codeList.getLabelsUnsorted();
        assertEquals(lExpectedIDs.length, lActual.length);
        for (i = 0; i<lActual.length; i++) {
            assertTrue(lExpLabels.indexOf(lActual[i]) > 0);
        }

        //test existElementID()
        try {
            codeList.existElementID("2");
        }
        catch (final org.hip.kernel.code.CodeNotFoundException exc) {
            fail(exc.getMessage());
        }
        try {
            codeList.existElementID("0");
            fail("Code with elementID 0 doesn't exist!");
        }
        catch (final org.hip.kernel.code.CodeNotFoundException exc) {
        }
    }

    @Test
    public void testGetElementIDByLabel() {
        try {
            assertEquals("1", codeList.getElementIDByLabel("Dies"));
            assertEquals("2", codeList.getElementIDByLabel("ist"));
            assertEquals("3", codeList.getElementIDByLabel("ein"));
            assertEquals("4", codeList.getElementIDByLabel("Test!"));
        }
        catch (final org.hip.kernel.code.CodeNotFoundException exc) {
            fail(exc.getMessage());
        }

        try {
            codeList.getElementIDByLabel("Test");
            fail("Label 'Test' doesn't exist!");
        }
        catch (final CodeNotFoundException exc) {}
        try {
            codeList.getElementIDByLabel("dies");
            fail("Label 'dies' doesn't exist!");
        }
        catch (final CodeNotFoundException exc) {}
        try {
            codeList.getElementIDByLabel("Hallo");
            fail("Label 'Hallo' doesn't exist!");
        }
        catch (final CodeNotFoundException exc) {}
    }

    @Test
    public void testGetLabel() {
        assertEquals("Dies", codeList.getLabel("1"));
        assertEquals("ist", codeList.getLabel("2"));
        assertEquals("ein", codeList.getLabel("3"));
        assertEquals("Test!", codeList.getLabel("4"));

        assertEquals(null, codeList.getLabel("5"));
    }

    @Test
    public void testToString() {
        final String lExpected1 = "1;4;3;2";
        final String lExpected2 = "Dies;Test!;ein;ist";

        assertEquals(lExpected1, codeList.toString());
        assertEquals(lExpected2, codeList.toLongString());

        final String lExpected3 = "<codeList> \n" +
                "<codeListItem id=\"1\" >Dies</codeListItem> \n" +
                "<codeListItem id=\"2\" selected=\"true\">ist</codeListItem> \n" +
                "<codeListItem id=\"3\" >ein</codeListItem> \n" +
                "<codeListItem id=\"4\" >Test!</codeListItem> \n" +
                "</codeList> \n";
        TestCode[] lSelected = new TestCode[1];
        lSelected[0] = new TestCode("2");
        assertEquals(lExpected3, codeList.toSelectionString(lSelected));

        final String lExpected4 = "<codeList> \n" +
                "<codeListItem id=\"1\" >Dies</codeListItem> \n" +
                "<codeListItem id=\"2\" selected=\"true\">ist</codeListItem> \n" +
                "<codeListItem id=\"3\" >ein</codeListItem> \n" +
                "<codeListItem id=\"4\" selected=\"true\">Test!</codeListItem> \n" +
                "</codeList> \n";
        lSelected = new TestCode[2];
        lSelected[0] = new TestCode("4");
        lSelected[1] = new TestCode("2");
        assertEquals(lExpected4, codeList.toSelectionString(lSelected));

        final Vector<AbstractCode> lSelVector = new Vector<AbstractCode>();
        lSelVector.add(new TestCode("4"));
        lSelVector.add(new TestCode("2"));
        assertEquals(lExpected4, codeList.toSelectionString(lSelVector));
    }
}
