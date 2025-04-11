package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.util.DefaultNameValueListVisitor;
import org.hip.kernel.util.NameValueListVisitor;
import org.hip.kernel.util.SortableItem;
import org.hip.kernel.util.VInvalidValueException;
import org.junit.jupiter.api.Test;

/**
 * SortableItemTest.java
 *
 * Created on 13.09.2002
 * @author Benno Luthiger
 */
public class SortableItemTest {

    @Test
    public void testToString() {
        final String lExpectedValue = "Test of a SortableItem";
        final String lExpectedString = "< org.hip.kernel.util.test.TestSortableItem value=\"Test of a SortableItem\" position=\"22\" />";
        try {
            final SortableItem lSortableItem = new TestSortableItem(lExpectedValue, 22);

            assertEquals(lExpectedValue, lSortableItem.getValue());
            assertEquals(lExpectedString, lSortableItem.toString());
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testEquals() {
        final String lValue1 = "Test of a SortableItem";
        final String lValue2 = "Test of a SortableItem2";

        try {
            final SortableItem lSortableItem1 = new TestSortableItem(lValue1, 22);
            final SortableItem lSortableItem2 = new TestSortableItem(lValue2, 22);
            final SortableItem lSortableItem3 = new TestSortableItem(lValue1, 10);
            final SortableItem lSortableItem4 = new TestSortableItem(lValue1, 22);

            assertTrue(lSortableItem1.equals(lSortableItem4));
            assertTrue(!lSortableItem1.equals(lSortableItem2));
            assertTrue(!lSortableItem1.equals(lSortableItem3));

            assertEquals(lSortableItem1.hashCode(), lSortableItem4.hashCode());
            assertTrue(lSortableItem1.hashCode() != lSortableItem2.hashCode());
            assertTrue(lSortableItem1.hashCode() != lSortableItem3.hashCode());
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testAccept() {
        final String lValue1 = "Test of a SortableItem";
        final String lValue2 = "Test of a SortableItem2";
        final String lExpected1 = "< org.hip.kernel.util.test.TestSortableItem <value='Test of a SortableItem' /> />";
        final String lExpected2 = "< org.hip.kernel.util.test.TestSortableItem <value='Test of a SortableItem2' /> />";
        try {
            final SortableItem lSortableItem1 = new TestSortableItem(lValue1, 22);
            final SortableItem lSortableItem2 = new TestSortableItem(lValue2, 10);

            NameValueListVisitor lVisitor = new DefaultNameValueListVisitor();
            lSortableItem1.accept(lVisitor);
            assertEquals(lExpected1, lVisitor.toString());

            lVisitor = new DefaultNameValueListVisitor();
            lSortableItem2.accept(lVisitor);
            assertEquals(lExpected2, lVisitor.toString());
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }
}
