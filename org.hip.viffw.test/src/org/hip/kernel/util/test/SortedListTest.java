package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.hip.kernel.exc.VException;
import org.hip.kernel.util.DefaultNameValueListVisitor;
import org.hip.kernel.util.NameValueListVisitor;
import org.hip.kernel.util.SortedList;
import org.hip.kernel.util.VInvalidSortCriteriaException;
import org.hip.kernel.util.VInvalidValueException;
import org.junit.jupiter.api.Test;

/**
 * SortedListTest.java
 *
 * Created on 13.09.2002
 * @author Benno Luthiger
 */
public class SortedListTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testSort() {
        final String lItem1 = "Z: Item for position 1";
        final String lItem2 = "Y: Item for position 2";
        final String lItem3 = "X: Item for position 3";
        final String lItem4 = "A: Item for position 4";
        final String[] lExpected = {lItem1, lItem2, lItem3, lItem4};

        final SortedList lSortedList = new TestSortedList();
        try {
            lSortedList.setValue(lItem4, 4);
            lSortedList.setValue(lItem2, 2);
            lSortedList.setValue(lItem3, 3);
            lSortedList.setValue(lItem1, 1);

            assertEquals(4, lSortedList.size());

            int i = 0;
            for (final Iterator<?> lItems = lSortedList.getItems(); lItems.hasNext();) {
                assertEquals("sorted position: " + i, lExpected[i], (String)((TestSortableItem)lItems.next()).getValue());
                i++;
            }
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testException() {
        final String lItem1 = "Z: Item for position 1";
        //		String lItem2 = "Y: Item for position 2";
        //		String lItem3 = "Z: Item for position 1";

        try {
            final SortedList lSortedList = new TestSortedList();
            lSortedList.setValue(lItem1, "a");
            fail("exception 1: shouldn't get here");
        }
        catch (final VInvalidSortCriteriaException exc) {
            assertNotNull(exc);
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }

        try {
            final SortedList lSortedList = new TestSortedList();
            lSortedList.setValue(null, 1);
            fail("exception 2: shouldn't get here");
        }
        catch (final VInvalidSortCriteriaException exc) {
            fail(exc.getMessage());
        }
        catch (final VInvalidValueException exc) {
            assertNotNull(exc);
        }
    }

    @Test
    public void testAccept() {
        final String lItem1 = "Z: Item for position 1";
        final String lItem2 = "Y: Item for position 2";
        final String lItem3 = "X: Item for position 3";
        final String lItem4 = "A: Item for position 4";
        final String lExpected = "<org.hip.kernel.util.test.TestSortedList >\n" +
                "\t< org.hip.kernel.util.test.TestSortableItem <value='Z: Item for position 1' /> />\n" +
                "\t< org.hip.kernel.util.test.TestSortableItem <value='Y: Item for position 2' /> />\n" +
                "\t< org.hip.kernel.util.test.TestSortableItem <value='X: Item for position 3' /> />\n" +
                "\t< org.hip.kernel.util.test.TestSortableItem <value='A: Item for position 4' /> />\n" +
                "</org.hip.kernel.util.test.TestSortedList>";

        final SortedList lSortedList = new TestSortedList();
        try {
            lSortedList.setValue(lItem4, 4);
            lSortedList.setValue(lItem2, 2);
            lSortedList.setValue(lItem3, 3);
            lSortedList.setValue(lItem1, 1);

            final NameValueListVisitor lVisitor = new DefaultNameValueListVisitor();
            lSortedList.accept(lVisitor);
            assertEquals(lExpected, lVisitor.toString());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testDublicates() {
        final String lItem1 = "Z: Item for position 1";
        final String lItem2 = "Y: Item for position 2";
        final String lItem3 = "Z: Item for position 1";

        try {
            SortedList lSortedList = new TestSortedList();
            lSortedList.setValue(lItem1, 1);
            lSortedList.setValue(lItem2, 2);
            lSortedList.setValue(lItem3, 1);
            assertEquals(2, lSortedList.size());

            NameValueListVisitor lVisitor = new DefaultNameValueListVisitor();
            lSortedList.accept(lVisitor);
            System.out.println(lVisitor.toString());

            lSortedList = new TestSortedList();
            lSortedList.setValue(lItem1, 1);
            lSortedList.setValue(lItem2, 2);
            lSortedList.setValue(lItem3, 3);
            assertEquals(3, lSortedList.size());

            lVisitor = new DefaultNameValueListVisitor();
            lSortedList.accept(lVisitor);
            System.out.println(lVisitor.toString());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }
}

