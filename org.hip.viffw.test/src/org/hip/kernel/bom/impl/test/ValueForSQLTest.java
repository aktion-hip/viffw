package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import org.hip.kernel.bom.impl.BetweenObjectImpl;
import org.hip.kernel.bom.impl.InObjectImpl;
import org.hip.kernel.bom.impl.ValueForSQL;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 */
public class ValueForSQLTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testGetValueAsString() {
        ValueForSQL lValue = new ValueForSQL("What's the purpose of the decimal ID?");
        assertEquals("What\\'s the purpose of the decimal ID?", lValue.getValueAsString());

        lValue = new ValueForSQL("This is a quote: \"Hallo World\"");
        assertEquals("This is a quote: \"Hallo World\"", lValue.getValueAsString());

        lValue = new ValueForSQL(new BigDecimal("9876.5432"));
        assertEquals("9876.5432", lValue.getValueAsString());

        final Date lDate = Date.valueOf("1989-01-20");
        lValue = new ValueForSQL(lDate);
        assertEquals("DATE('1989-01-20')", lValue.getValueAsString());

        lValue = new ValueForSQL(new Timestamp(lDate.getTime()));
        assertEquals("TIMESTAMP('1989-01-20 00:00:00')", lValue.getValueAsString());

        final Date lTo = Date.valueOf("1996-02-21");
        final BetweenObjectImpl lBetween = new BetweenObjectImpl(lDate, lTo);
        lValue = new ValueForSQL(lBetween);
        assertEquals("BETWEEN DATE('1989-01-20') AND DATE('1996-02-21')", lValue.getValueAsString());

        final InObjectImpl lInObject = new InObjectImpl(
                new Object[] { Integer.valueOf(55), Integer.valueOf(57), Integer.valueOf(78) });
        lValue = new ValueForSQL(lInObject);
        assertEquals("IN (55, 57, 78)", lValue.getValueAsString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testGetValueForPrepared() {
        ValueForSQL lValue = new ValueForSQL("What's the purpose of the decimal ID?");
        assertEquals("?", lValue.getValueForPrepared());

        lValue = new ValueForSQL("This is a quote: \"Hallo World\"");
        assertEquals("?", lValue.getValueForPrepared());

        lValue = new ValueForSQL(new BigDecimal("9876.5432"));
        assertEquals("?", lValue.getValueForPrepared());

        final Date lDate = Date.valueOf("1989-01-20");
        lValue = new ValueForSQL(lDate);
        assertEquals("?", lValue.getValueForPrepared());

        lValue = new ValueForSQL(new Timestamp(lDate.getTime()));
        assertEquals("?", lValue.getValueForPrepared());

        final Date lTo = Date.valueOf("1996-02-21");
        final BetweenObjectImpl lBetween = new BetweenObjectImpl(lDate, lTo);
        lValue = new ValueForSQL(lBetween);
        assertEquals("BETWEEN ? AND ?", lValue.getValueForPrepared());

        final InObjectImpl lInObject = new InObjectImpl(
                new Object[] { Integer.valueOf(55), Integer.valueOf(57), Integer.valueOf(78) });
        lValue = new ValueForSQL(lInObject);
        assertEquals("IN (?, ?, ?)", lValue.getValueForPrepared());
    }

}
