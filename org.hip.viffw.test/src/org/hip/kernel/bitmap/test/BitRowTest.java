package org.hip.kernel.bitmap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bitmap.BitRow;
import org.hip.kernel.bitmap.BitRowImpl;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 */
public class BitRowTest {

    @Test
    public void testDo() {
        final BitRow lBits1 = new BitRowImpl(8);

        assertEquals(0, lBits1.getBitValue());

        lBits1.setBit(1, true);
        assertEquals(2, lBits1.getBitValue());

        lBits1.setBit(1, false);
        assertEquals(0, lBits1.getBitValue());

        lBits1.setBitValue(8);
        assertTrue(lBits1.getBit(3));

        lBits1.setBit(7, true);
        lBits1.setBitValue(8);
        assertTrue(lBits1.getBit(3));

        lBits1.setBitValue(1);
        final BitRow lBits2 = lBits1.invert();
        assertEquals(254, lBits2.getBitValue());

        lBits1.setBitValue(511);
        assertEquals(255, lBits1.getBitValue());

        lBits2.setBitValue(1);
        assertEquals(1, lBits1.and(lBits2).getBitValue());
        assertEquals(255, lBits1.or(lBits2).getBitValue());
        assertEquals(254, lBits1.xor(lBits2).getBitValue());

        lBits1.setBitValue(254);
        assertEquals(0, lBits1.and(lBits2).getBitValue());
        assertEquals(255, lBits1.or(lBits2).getBitValue());
        assertEquals(255, lBits1.xor(lBits2).getBitValue());


        lBits2.setBitValue(254);
        assertEquals(254, lBits1.and(lBits2).getBitValue());
        assertEquals(254, lBits1.or(lBits2).getBitValue());
        assertEquals(0, lBits1.xor(lBits2).getBitValue());
        try {
            lBits1.setBit(9, false);
            fail("Shouldn't get here!");
        }
        catch (final ArrayIndexOutOfBoundsException exc) {}

        assertTrue(lBits1.equals(lBits2));
        lBits2.setBitValue(10);
        assertTrue(!lBits1.equals(lBits2));
        assertTrue(!lBits1.equals("test"));

        assertEquals("<BitRow>11111110</BitRow>", lBits1.toString());
    }
}
