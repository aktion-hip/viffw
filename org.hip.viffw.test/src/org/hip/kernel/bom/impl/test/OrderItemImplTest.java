package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.OrderItem;
import org.hip.kernel.bom.impl.OrderItemImpl;
import org.hip.kernel.util.VInvalidValueException;
import org.junit.jupiter.api.Test;

/**
 * OrderItemImplTest.java
 *
 * Created on 15.09.2002
 * @author Benno Luthiger
 */
public class OrderItemImplTest {

    @Test
    public void testEquals() {
        final String lValue1 = "fColumnName";
        final String lValue2 = "fColumnName2";

        try {
            final OrderItem lOrderItem1 = new OrderItemImpl(lValue1, true, 1);
            final OrderItem lOrderItem2 = new OrderItemImpl(lValue2, true, 1);
            final OrderItem lOrderItem3 = new OrderItemImpl(lValue1, false, 1);
            final OrderItem lOrderItem4 = new OrderItemImpl(lValue1, true, 2);
            final OrderItem lOrderItem5 = new OrderItemImpl(lValue1, true, 1);

            assertTrue(lOrderItem1.equals(lOrderItem5));
            assertTrue(!lOrderItem1.equals(lOrderItem2));
            assertTrue(!lOrderItem1.equals(lOrderItem3));
            assertTrue(!lOrderItem1.equals(lOrderItem4));

            assertEquals(lOrderItem1.hashCode(), lOrderItem5.hashCode());
            assertTrue(lOrderItem1.hashCode() != lOrderItem2.hashCode());
            assertTrue(lOrderItem1.hashCode() != lOrderItem3.hashCode());
            assertTrue(lOrderItem1.hashCode() != lOrderItem4.hashCode());
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testToString() {
        final String lValue1 = "fColumnName1";
        final String lValue2 = "fColumnName2";
        final String lExpected1 = "< org.hip.kernel.bom.impl.OrderItemImpl value=\"fColumnName1\" position=\"1\" direction=\"DESC\" />";
        final String lExpected2 = "< org.hip.kernel.bom.impl.OrderItemImpl value=\"fColumnName2\" position=\"2\" direction=\"ASC\" />";
        try {
            final OrderItem lOrderItem1 = new OrderItemImpl(lValue1, true, 1);
            final OrderItem lOrderItem2 = new OrderItemImpl(lValue2, false, 2);

            assertEquals(lExpected1, lOrderItem1.toString());
            assertEquals(lExpected2, lOrderItem2.toString());
        }
        catch (final VInvalidValueException exc) {
            fail(exc.getMessage());
        }
    }
}
