package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.util.DefaultNameValue;
import org.junit.jupiter.api.Test;

/**
 * 	Test cases to test the implementation of DefaultNameValue
 *
 *	@author	Benno Luthiger
 */
public class DefaultNameValueTest {

    @Test
    public void testEquals() {
        final DefaultNameValue lNameValue1 = new DefaultNameValue(null, "firstName", "Adam");
        final DefaultNameValue lNameValue2 = new DefaultNameValue( null, "firstName", "Eva");
        final DefaultNameValue lNameValue3 = lNameValue1;
        final DefaultNameValue lNameValue4 = new DefaultNameValue( null, "firstName", "Adam");

        assertTrue(lNameValue1 == lNameValue3);
        assertTrue(lNameValue1 != lNameValue4);
        assertTrue(lNameValue1.equals(lNameValue4));
        assertTrue(!lNameValue1.equals(lNameValue2));
        assertTrue(!lNameValue1.equals(null));
    }

    @Test
    public void testToString() {
        final DefaultNameValue lNameValue1 = new DefaultNameValue( null, "firstName", "Adam");
        final DefaultNameValue lNameValue2 = new DefaultNameValue( null, "firstName", "Eva");
        final DefaultNameValue lNameValue3 = new DefaultNameValue( null, "firstName", null);
        final DefaultNameValue lNameValue4 = new DefaultNameValue( null, "lastName", lNameValue1);

        final String lExpcected1 = "< org.hip.kernel.util.DefaultNameValue name=\"firstName\" value=\"Adam\" />";
        final String lExpcected2 = "< org.hip.kernel.util.DefaultNameValue name=\"firstName\" value=\"Eva\" />";
        final String lExpcected3 = "< org.hip.kernel.util.DefaultNameValue name=\"firstName\" value=\"null\" />";
        final String lExpcected4 = "< org.hip.kernel.util.DefaultNameValue name=\"lastName\" value=\"< org.hip.kernel.util.DefaultNameValue name=\"firstName\" value=\"Adam\" />\" />";

        assertEquals(lExpcected1, lNameValue1.toString());
        assertEquals(lExpcected2, lNameValue2.toString());
        assertEquals(lExpcected3, lNameValue3.toString());
        assertEquals(lExpcected4, lNameValue4.toString());
    }
}
