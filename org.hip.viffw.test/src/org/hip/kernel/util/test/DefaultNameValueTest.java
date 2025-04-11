package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.hip.kernel.util.DefaultNameValue;
import org.hip.kernel.util.NameValue;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class DefaultNameValueTest {

    @Test
    public void testDo() {
        final String lName = "Test";
        final Object lValue = new Integer(100);
        final NameValue lNameValue = new DefaultNameValue(null, lName, lValue);

        assertEquals(lName, lNameValue.getName());
        assertEquals(lValue, lNameValue.getValue());

        // setName() does nothing
        try {
            lNameValue.setName(lName + "!!");
            assertEquals(lName, lNameValue.getName());
        } catch (final org.hip.kernel.util.VInvalidNameException exc) {
            fail(exc.getMessage());
        }

        // set() does nothing
        try {
            lNameValue.set(lName + "!!", lName + "!!!");
            assertEquals(lName, lNameValue.getName());
            assertEquals(lValue, lNameValue.getValue());
        } catch (final org.hip.kernel.util.VInvalidNameException exc) {
            fail(exc.getMessage());
        } catch (final org.hip.kernel.util.VInvalidValueException exc) {
            fail(exc.getMessage());
        }

        // setValue() changes the value
        try {
            lNameValue.setValue(lName + "!!!");
            assertEquals(lName + "!!!", lNameValue.getValue());
        } catch (final org.hip.kernel.util.VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testEquals() {
        final String lName1 = "Test";
        final Object lValue1 = new Integer(100);
        final NameValue lNameValue1 = new DefaultNameValue(null, lName1, lValue1);

        final String lName2 = "Test2";
        final Object lValue2 = "Value";
        final NameValue lNameValue2 = new DefaultNameValue(null, lName2, lValue2);

        final NameValue lNameValue3 = new DefaultNameValue(null, lName1, lValue1);

        assertTrue(lNameValue1.equals(lNameValue3));
        assertEquals(lNameValue1.hashCode(), lNameValue3.hashCode());

        assertTrue(!lNameValue1.equals(lNameValue2));
        assertTrue(lNameValue1.hashCode() != lNameValue2.hashCode());

        // change the value of lNameValue3
        try {
            lNameValue3.setValue(lValue2);
        } catch (final org.hip.kernel.util.VInvalidValueException exc) {
            fail(exc.getMessage());
        }
        assertTrue(!lNameValue1.equals(lNameValue3));
        assertTrue(lNameValue1.hashCode() != lNameValue3.hashCode());
        assertTrue(!lNameValue1.equals(lNameValue3));
        assertTrue(lNameValue1.hashCode() != lNameValue3.hashCode());

        // change back the value of lNameValue3
        try {
            lNameValue3.setValue(lValue1);
        } catch (final org.hip.kernel.util.VInvalidValueException exc) {
            fail(exc.getMessage());
        }
        assertTrue(lNameValue1.equals(lNameValue3));
        assertEquals(lNameValue1.hashCode(), lNameValue3.hashCode());
        assertTrue(!lNameValue3.equals(lNameValue2));
        assertTrue(lNameValue3.hashCode() != lNameValue2.hashCode());

    }

    @Test
    public void testExtract() {

        final String lName1 = "Test";
        final Object lValue1 = new Integer(100);
        final String lName2 = "Test2";
        final Object lValue2 = "Value";

        final String lToExtract = lName1 + "=" + lValue1.toString() + ", " +
                lName2 + "=" + lValue2 + ", " +
                lName1 + "=" + lValue1.toString();
        final List<NameValue> lExtracted = DefaultNameValue.extract(lToExtract);

        final NameValue lNameValue1 = new DefaultNameValue(null, lName1, lValue1.toString());
        final NameValue lNameValue2 = new DefaultNameValue(null, lName2, lValue2);
        final NameValue lNameValue3 = new DefaultNameValue(null, lName1, lValue1.toString());
        final NameValue[] lExpected = { lNameValue1, lNameValue2, lNameValue3 };
        int i = 0;
        for (final NameValue lNameValue : lExtracted) {
            assertTrue(lExpected[i++].equals(lNameValue));
        }
    }

}
