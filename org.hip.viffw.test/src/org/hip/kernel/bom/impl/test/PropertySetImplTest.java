package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.hip.kernel.bom.Property;
import org.hip.kernel.bom.PropertySet;
import org.hip.kernel.bom.impl.PropertySetImpl;
import org.hip.kernel.exc.VException;
import org.hip.kernel.util.NameValue;
import org.junit.jupiter.api.Test;

/**
 * 	TestCases to test the PropertySet implementation.
 *
 *	@author	Benno Luthiger
 */
public class PropertySetImplTest {
    private static final String[] EXPECTED_NAMES = new String[] {"firstName", "lastName"};
    private static final String[] EXPECTED_VALUES1 = new String[] {"Adam", "Blunder"};
    private static final String[] EXPECTED_VALUES2 = new String[] {"Bach", "Eva"};


    @SuppressWarnings("deprecation")
    @Test
    public void testBasic() throws VException {
        final PropertySet lPropertySet = new PropertySetImpl( null );
        assertNotNull( lPropertySet );

        lPropertySet.setValue(EXPECTED_NAMES[0], EXPECTED_VALUES1[0]);
        assertEquals(1, lPropertySet.size());

        lPropertySet.setValue(EXPECTED_NAMES[1], EXPECTED_VALUES1[1]);
        assertEquals(2, lPropertySet.size());

        final Collection<String> lNames = lPropertySet.getNames2();
        for (int i = 0; i < EXPECTED_NAMES.length; i++) {
            assertTrue(lNames.contains(EXPECTED_NAMES[i]));
        }
        Collection<String> lExpected = Arrays.asList(EXPECTED_VALUES1);
        for (final NameValue lNameValue : lPropertySet.getNameValues2()) {
            assertTrue(lExpected.contains(lNameValue.getValue()));
        }

        lPropertySet.setValue(null, "testNull");
        assertEquals(2, lPropertySet.size());

        Iterator<Property> lChanged = lPropertySet.getChangedProperties();
        assertTrue(!lChanged.hasNext());

        lPropertySet.setValue("firstName", "Eva");
        int lCount = 0;
        for (lChanged = lPropertySet.getChangedProperties(); lChanged.hasNext();) {
            lChanged.next();
            lCount++;
        }
        assertEquals(1, lCount);
        assertEquals(1, lPropertySet.getChangedProperties2().size());

        lPropertySet.setValue("lastName", "Bach");
        assertEquals(2, lPropertySet.getChangedProperties2().size());

        lExpected = Arrays.asList(EXPECTED_VALUES2);
        for (final NameValue lNameValue : lPropertySet.getNameValues2()) {
            assertTrue(lExpected.contains(lNameValue.getValue()));
        }

        lPropertySet.setVirgin();
        assertEquals(2, lPropertySet.size());
        assertTrue(!lChanged.hasNext());
    }

    @Test
    public void testEquals() throws VException {
        final String lName1 = "firstName";
        final String lName2 = "lastName";
        final String lValue1 = "Adam";
        final String lValue2 = "Blunder";

        final PropertySet lPropertySet1 = new PropertySetImpl( null );
        assertNotNull( lPropertySet1 );

        PropertySet lPropertySet2 = new PropertySetImpl( null );
        assertNotNull( lPropertySet2 );

        lPropertySet1.setValue(lName1, lValue1);
        lPropertySet1.setValue(lName2, lValue2);

        lPropertySet2.setValue(lName1, lValue1);
        lPropertySet2.setValue(lName2, lValue2);

        assertTrue(lPropertySet1.equals(lPropertySet2));
        assertEquals(lPropertySet1.hashCode(), lPropertySet2.hashCode());

        //properties are equal if their name is equal
        //therefore the propertysets are equal even if their values differ!
        lPropertySet1.setValue(lName2, lPropertySet2);
        assertTrue(lPropertySet1.equals(lPropertySet2));
        assertEquals(lPropertySet1.hashCode(), lPropertySet2.hashCode());

        lPropertySet1.setValue(lName2, lValue2);
        assertTrue(lPropertySet1.equals(lPropertySet2));
        assertEquals(lPropertySet1.hashCode(), lPropertySet2.hashCode());

        lPropertySet1.setValue("test", new Integer(12));
        assertTrue(!lPropertySet1.equals(lPropertySet2));
        assertTrue(lPropertySet1.hashCode() != lPropertySet2.hashCode());

        lPropertySet2 = new PropertySetImpl( null );
        lPropertySet2.setValue(lName1, lValue1);
        lPropertySet2.setValue("test", lValue2);
        assertTrue(!lPropertySet1.equals(lPropertySet2));
        assertTrue(lPropertySet1.hashCode() != lPropertySet2.hashCode());
    }
}
