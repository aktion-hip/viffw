package org.hip.kernel.bitmap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.bitmap.BitField;
import org.hip.kernel.bitmap.BitFieldImpl;
import org.hip.kernel.bitmap.BitRow;
import org.hip.kernel.bitmap.BitRowImpl;
import org.hip.kernel.bitmap.MatrixPosition;
import org.hip.kernel.bitmap.MatrixPositions;
import org.hip.kernel.bitmap.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** @author Benno Luthiger */
public class BitFieldTest {
    private BitField field;
    private static int columnSize = 8;

    @BeforeEach
    public void setUp() throws Exception {
        final int columnSize = 8;
        this.field = new BitFieldImpl();

        BitRow lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(1);
        this.field.addRow("row 1", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(2);
        this.field.addRow("row 2", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(4);
        this.field.addRow("row 3", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(8);
        this.field.addRow("row 4", lRow);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDo() {
        assertTrue(this.field.getBit(2, 2));
        assertTrue(!this.field.getBit(1, 2));
        assertTrue(this.field.getBit(3, 3));
        assertTrue(!this.field.getBit(3, 4));

        final BitField lField2 = this.field.invert();
        assertTrue(!lField2.getBit(2, 2));
        assertTrue(lField2.getBit(1, 2));
        assertTrue(!lField2.getBit(3, 3));
        assertTrue(lField2.getBit(3, 4));

        final BitField lField3 = this.field.and(lField2);
        int lSum = 0;
        for (int i = 0; i < lField3.getRowSize(); i++) {
            lSum += lField3.getBitRow(i).getBitValue();
        }
        assertEquals(0, lSum);

        assertTrue(!this.field.equals(lField2));
        assertTrue(!this.field.equals(lField3));
        assertTrue(!this.field.equals("test"));

        final BitField lField4 = new BitFieldImpl();

        BitRow lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(1);
        lField4.addRow("row 1", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(2);
        lField4.addRow("row 2", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(4);
        lField4.addRow("row 3", lRow);
        lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(8);
        lField4.addRow("row 4", lRow);

        assertTrue(this.field.equals(lField4));
        assertEquals(this.field.hashCode(), lField4.hashCode());

        final MatrixPosition lPosition = new MatrixPosition(2, 2);
        assertTrue(this.field.getBit(lPosition));

        this.field.setBit(lPosition, false);
        assertTrue(!this.field.equals(lField4));
        this.field.setBit(lPosition, true);
        assertTrue(this.field.equals(lField4));

        this.field.setBit(lPosition, false);
        final MatrixPositions lDiffs = this.field.getDifferences(lField4);
        assertEquals(1, lDiffs.size());
        assertEquals(lPosition, lDiffs.get(0));

        Tuple lTuple = this.field.removeRow(1);
        assertEquals(2, lTuple.getTupleBitRow().getBitValue());
        assertEquals(3, this.field.getRowSize());
        lTuple = this.field.getTuple(2);
        assertEquals(8, lTuple.getTupleBitRow().getBitValue());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final BitField lField2 = ((BitFieldImpl) this.field).clone();
        assertFalse(this.field == lField2);

        assertFalse(lField2.getBit(2, 2));
        assertFalse(lField2.getBit(1, 2));
        assertFalse(lField2.getBit(3, 3));
        assertFalse(lField2.getBit(3, 4));

        final BitField lField3 = ((BitFieldImpl) this.field).clone();
        assertFalse(this.field == lField3);
        assertEquals(this.field.getRowSize(), lField3.getRowSize());

        final BitRow lRow = new BitRowImpl(columnSize);
        lRow.setBitValue(1);
        lField3.addRow("123", lRow);
        assertFalse(this.field.getRowSize() == lField3.getRowSize());
    }
}
