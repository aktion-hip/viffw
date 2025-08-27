package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;

import org.hip.kernel.bom.impl.BetweenObjectImpl;
import org.junit.jupiter.api.Test;

public class BetweenObjectImplTest {

    @Test
    public void testToString() {
        final Date lDate1 = Date.valueOf("1989-08-20");
        final Date lDate2 = Date.valueOf("1999-04-24");

        BetweenObjectImpl lBetween = new BetweenObjectImpl(lDate1, lDate2);
        assertEquals(lBetween.toString(), "BETWEEN DATE('1989-08-20') AND DATE('1999-04-24')");

        lBetween = new BetweenObjectImpl(lDate2, lDate1);
        assertEquals(lBetween.toString(), "BETWEEN DATE('1989-08-20') AND DATE('1999-04-24')");

        lBetween = new BetweenObjectImpl(new Timestamp(lDate1.getTime()), new Timestamp(lDate2.getTime()));
        assertEquals(lBetween.toString(),
                "BETWEEN TIMESTAMP('1989-08-20 00:00:00') AND TIMESTAMP('1999-04-24 00:00:00')");
    }

    @Test
    public void testToPrepared() {
        final Date lDate1 = Date.valueOf("1989-08-20");
        final Date lDate2 = Date.valueOf("1999-04-24");

        BetweenObjectImpl lBetween = new BetweenObjectImpl(lDate1, lDate2);
        assertEquals(lBetween.toPrepared(), "BETWEEN ? AND ?");

        lBetween = new BetweenObjectImpl(new Timestamp(lDate1.getTime()), new Timestamp(lDate2.getTime()));
        assertEquals(lBetween.toPrepared(), "BETWEEN ? AND ?");
    }

}
