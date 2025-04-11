package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.hip.kernel.bom.SingleValueQueryStatement;
import org.hip.kernel.bom.impl.SingleValueQueryStatementImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class SingleValueQueryStatementImplTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        SingleValueQueryStatement lStatement = new SingleValueQueryStatementImpl();
        final String lSQL = "SELECT COUNT(TestID) FROM tblTest";

        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        final String[] lFirstnames = {"Eva", "Pustekuchen", "Fi"};

        assertNotNull(lStatement);
        assertEquals(0, lStatement.executeQuery(lSQL).intValue());

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2], lFirstnames[2]);
        assertEquals(1, lStatement.executeQuery(lSQL).intValue());
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1], lFirstnames[1]);
        assertEquals(2, lStatement.executeQuery(lSQL).intValue());
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0], lFirstnames[0]);
        assertEquals(3, lStatement.executeQuery(lSQL).intValue());

        lStatement = new SingleValueQueryStatementImpl();
        Collection<Object> lResult = lStatement.executeQuery2("SELECT COUNT(TESTID), COUNT(SNAME) FROM tblTest");
        final Iterator<Object> lValues = lResult.iterator();
        assertEquals(2, lResult.size());
        for (int i = 0; i < 2; i++) {
            assertEquals("number of entries 3."+i, "3", lValues.next().toString());
        }

        lResult = lStatement.executeQuery2("SELECT MIN(FAMOUNT), MAX(DTMUTATION) FROM tblTest");
        assertEquals(2, lResult.size());
    }
}
