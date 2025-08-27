package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.OrderItem;
import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.kernel.util.SortableItem;
import org.hip.kernel.util.VInvalidSortCriteriaException;
import org.hip.kernel.util.VInvalidValueException;
import org.junit.jupiter.api.Test;

/**
 * OrderObjectImplTest.java
 *
 * Created on 15.09.2002
 * @author Benno Luthiger
 */
public class OrderObjectImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testSetValue() {
        final String lColumn1 = "sColumn1";
        final String lColumn2 = "sColumn2";
        final String lColumn3 = "sColumn3";
        final String[] lColumns = {lColumn1, lColumn2, lColumn3};

        OrderObject lOrderObject = new OrderObjectImpl();
        try {
            lOrderObject.setValue(lColumn1, true, 1);
            lOrderObject.setValue(lColumn3, false, 3);
            lOrderObject.setValue(lColumn2, true, 2);

            assertEquals(3, lOrderObject.size());

            int i = 0;
            for (final SortableItem lItem : lOrderObject.getItems2()) {
                assertEquals(lColumns[i], lItem.getValue().toString());
                i++;
            }
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }

        lOrderObject = new OrderObjectImpl();
        try {
            lOrderObject.setValue("sColumn1", 1);
            assertTrue(!((OrderItem) lOrderObject.getItems().next()).isDescending());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }

        lOrderObject = new OrderObjectImpl();
        try {
            lOrderObject.setValue(new Integer(12), 1);
            fail("exception 1: shouldn't get here 1");
        }
        catch (final VInvalidValueException exc) {
            assertNotNull(exc);
        }
        catch (final VInvalidSortCriteriaException exc) {
            fail(exc.getMessage());
        }

        lOrderObject = new OrderObjectImpl();
        try {
            lOrderObject.setValue("sColumn1", "a");
            fail("exception 2: shouldn't get here 2");
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
        catch (final VInvalidSortCriteriaException exc) {
            assertNotNull(exc);
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testEmpty() {
        final OrderObject lOrder = new OrderObjectImpl();
        assertFalse(lOrder.getItems().hasNext());
    }

    @Test
    public void testEquals() {
        final OrderObject lOrderObject1 = new OrderObjectImpl();
        final OrderObject lOrderObject2 = new OrderObjectImpl();
        final OrderObject lOrderObject3 = new OrderObjectImpl();
        final OrderObject lOrderObject4 = new OrderObjectImpl();

        try {
            lOrderObject1.setValue("sColumn1", true, 1);
            lOrderObject1.setValue("sColumn2", true, 2);
            lOrderObject1.setValue("sColumn3", true, 3);

            lOrderObject2.setValue("sColumn1", false, 1);
            lOrderObject2.setValue("sColumn2", true, 2);
            lOrderObject2.setValue("sColumn3", true, 3);

            lOrderObject3.setValue("sColumn1", true, 1);
            lOrderObject3.setValue("sColumn2", true, 3);
            lOrderObject3.setValue("sColumn3", true, 2);

            lOrderObject4.setValue("sColumn1", true, 1);
            lOrderObject4.setValue("sColumn2", true, 2);
            lOrderObject4.setValue("sColumn3", true, 3);

            assertTrue(lOrderObject1.equals(lOrderObject4));
            assertTrue(!lOrderObject1.equals(lOrderObject2));
            assertTrue(!lOrderObject1.equals(lOrderObject3));

            assertEquals(lOrderObject1.hashCode(), lOrderObject4.hashCode());
            assertTrue(lOrderObject1.hashCode() != lOrderObject2.hashCode());
            assertEquals(lOrderObject1.hashCode(), lOrderObject3.hashCode());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testToString() {
        final String lExpected =
                "<org.hip.kernel.bom.impl.OrderObjectImpl >\n" +
                        "\t< org.hip.kernel.bom.impl.OrderItemImpl <value='sColumn1' /> />\n" +
                        "\t< org.hip.kernel.bom.impl.OrderItemImpl <value='sColumn2' /> />\n" +
                        "\t< org.hip.kernel.bom.impl.OrderItemImpl <value='sColumn3' /> />\n" +
                        "</org.hip.kernel.bom.impl.OrderObjectImpl>";
        final OrderObject lOrderObject = new OrderObjectImpl();
        try {
            lOrderObject.setValue("sColumn1", true, 1);
            lOrderObject.setValue("sColumn2", false, 2);
            lOrderObject.setValue("sColumn3", true, 3);
            assertEquals(lExpected, lOrderObject.toString());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }
}
