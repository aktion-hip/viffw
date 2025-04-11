package org.hip.kernel.bitmap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.bitmap.BitRow;
import org.hip.kernel.bitmap.BitRowImpl;
import org.hip.kernel.bitmap.Tuple;
import org.junit.jupiter.api.Test;

/** @author Benno Luthiger */
public class TupleTest {

    @Test
    public void testClone() throws CloneNotSupportedException {
        final BitRow lBits = new BitRowImpl(8);
        lBits.setBitValue(12);
        final Tuple lTuple1 = new Tuple("original", lBits);
        final Tuple lTuple2 = lTuple1.clone();

        assertEquals(lTuple1, lTuple2);
        assertEquals(12, lTuple2.getTupleBitRow().getBitValue());

        lTuple2.getTupleBitRow().setBitValue(8);
        assertTrue(!lTuple1.equals(lTuple2));
        assertEquals(12, lTuple1.getTupleBitRow().getBitValue());
        assertEquals(8, lTuple2.getTupleBitRow().getBitValue());
    }
}
