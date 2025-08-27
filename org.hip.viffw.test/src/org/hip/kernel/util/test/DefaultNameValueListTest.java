package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import org.hip.kernel.util.DefaultNameValue;
import org.hip.kernel.util.DefaultNameValueList;
import org.hip.kernel.util.NameValue;
import org.hip.kernel.util.NameValueList;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class DefaultNameValueListTest {

    @Test
    public void testBasic() throws Exception {
        final String lName1 = "firstName";
        final String lName2 = "lastName";
        final String lValue1 = "Adam";
        final String lValue2 = "Blunder";
        final String lValue3 = "testNull";
        final String lValue4 = "Eva";
        final String lValue5 = "Bach";
        final Integer lValue6 = new Integer(100);
        final Integer lValue7 = new Integer(150);

        final String[] lExpNames = {lName1, lName2};
        final Vector<String> lVExpNames = new Vector<String>(Arrays.asList(lExpNames));

        final NameValueList lNameValueList = new DefaultNameValueList();
        assertNotNull(lNameValueList);

        lNameValueList.setValue(lName1, lValue1);
        assertEquals(1, lNameValueList.size());
        assertEquals(lValue1, lNameValueList.getValue(lName1));
        assertEquals(lValue1, lNameValueList.get(lName1).getValue());

        lNameValueList.setValue(lName2, lValue2);
        assertEquals(2, lNameValueList.size());
        assertEquals(lValue2, lNameValueList.getValue(lName2));
        assertEquals(lValue2, lNameValueList.get(lName2).getValue());

        lNameValueList.setValue(null, lValue3);
        assertEquals(2, lNameValueList.size());

        lNameValueList.setValue(lName1, lValue4);
        assertEquals(2, lNameValueList.size());
        assertEquals(lValue4, lNameValueList.getValue(lName1));
        assertEquals(lValue4, lNameValueList.get(lName1).getValue());

        lNameValueList.setValue(lName2, lValue5);
        assertEquals(2, lNameValueList.size());
        assertEquals(lValue5, lNameValueList.getValue(lName2));
        assertEquals(lValue5, lNameValueList.get(lName2).getValue());

        lNameValueList.setValue(lName2, lValue6);
        assertEquals(2, lNameValueList.size());
        assertEquals(lValue6, lNameValueList.getValue(lName2));
        assertEquals(lValue6, lNameValueList.get(lName2).getValue());

        for (final String lName : lNameValueList.getNames2()) {
            assertTrue(lVExpNames.contains(lName));
        }

        final NameValue lNameValue1 = new DefaultNameValue(null, lName1, lValue4);
        final NameValue lNameValue2 = new DefaultNameValue(null, lName2, lValue6);
        final NameValue[] lExpNameValues = {lNameValue1, lNameValue2};
        final Vector<Object> lVExpNameValues = new Vector<Object>(Arrays.asList(lExpNameValues));
        for (final Object lNameValue : lNameValueList.getNameValues2()) {
            assertTrue(lVExpNameValues.contains(lNameValue));
        }

        //adding a new NameValue with an Name already existing in the List doesn't change the size
        final NameValue lNameValue3 = new DefaultNameValue(null, lName1, lValue7);
        lNameValueList.add(lNameValue3);
        assertEquals(2, lNameValueList.size());
        assertEquals(lValue7, lNameValueList.getValue(lName1));
        assertEquals(lValue7, lNameValueList.get(lName1).getValue());

        //adding a new NameValue
        final String lName3 = "New";
        final NameValue lNameValue4 = new DefaultNameValue(null, lName3, lValue7);
        lNameValueList.add(lNameValue4);
        assertEquals(3, lNameValueList.size());
        assertEquals(lValue7, lNameValueList.getValue(lName3));
        assertEquals(lValue7, lNameValueList.get(lName3).getValue());
    }

    public void testEquals() throws Exception {
        final String lName1 = "firstName";
        final String lName2 = "lastName";
        final String lValue1 = "Adam";
        final String lValue2 = "Blunder";

        final NameValueList lNameValueList1 = new DefaultNameValueList();
        assertNotNull( lNameValueList1 );

        NameValueList lNameValueList2 = new DefaultNameValueList();
        assertNotNull( lNameValueList2 );

        lNameValueList1.setValue(lName1, lValue1);
        lNameValueList1.setValue(lName2, lValue2);

        lNameValueList2.setValue(lName1, lValue1);
        lNameValueList2.setValue(lName2, lValue2);

        assertTrue(lNameValueList1.equals(lNameValueList2));
        assertEquals(lNameValueList1.hashCode(), lNameValueList2.hashCode());

        //NameValues are equal if their name and value is equal
        lNameValueList1.setValue(lName2, lNameValueList2);
        assertTrue(!lNameValueList1.equals(lNameValueList2));
        assertTrue(lNameValueList1.hashCode() != lNameValueList2.hashCode());

        lNameValueList1.setValue(lName2, lValue2);
        assertTrue(lNameValueList1.equals(lNameValueList2));
        assertEquals(lNameValueList1.hashCode(), lNameValueList2.hashCode());

        lNameValueList1.setValue("test", new Integer(12));
        assertTrue(!lNameValueList1.equals(lNameValueList2));
        assertTrue(lNameValueList1.hashCode() != lNameValueList2.hashCode());

        lNameValueList2 = new DefaultNameValueList();
        lNameValueList2.setValue(lName1, lValue1);
        lNameValueList2.setValue("test", lValue2);
        assertTrue(!lNameValueList1.equals(lNameValueList2));
        assertTrue(lNameValueList1.hashCode() != lNameValueList2.hashCode());
    }
}
