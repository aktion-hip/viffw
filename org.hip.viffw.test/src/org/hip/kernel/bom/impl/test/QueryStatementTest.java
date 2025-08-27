package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.QueryStatement;
import org.hip.kernel.bom.impl.DefaultQueryStatement;
import org.hip.kernel.bom.impl.DefaultStatement;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class QueryStatementTest {

    @AfterEach
    void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    void testDo() throws Exception {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        final String lSQL = "SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName";
        final String lExpected1 = "< org.hip.kernel.bom.impl.DefaultQueryStatement SQL=\"null\" />";
        final String lExpected2 = "< org.hip.kernel.bom.impl.DefaultQueryStatement SQL=\"SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName\" />";

        final QueryStatement lStatement = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        assertEquals(lExpected1, lStatement.toString());

        final QueryResult lResult = lStatement.executeQuery(lSQL);

        int i = 0;
        while (lResult.hasMoreElements()) {
            final GeneralDomainObject lModel = lResult.next();
            assertEquals(lNames[i++], lModel.get(Test2DomainObjectHomeImpl.KEY_NAME));
        }
        assertEquals(3, i);
        assertEquals(lSQL, ((DefaultQueryStatement)lStatement).getSQLString());
        assertEquals(lExpected2, lStatement.toString());
    }

    @Test
    void testEquals() throws Exception {
        final String lSQL = "SELECT * FROM tblTest ORDER BY sName";

        final QueryStatement lStatement1 = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        lStatement1.executeQuery(lSQL);

        final QueryStatement lStatement2 = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        lStatement2.executeQuery("SELECT * FROM tblTest");

        final QueryStatement lStatement3 = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        lStatement3.executeQuery(lSQL);

        assertTrue(lStatement1.equals(lStatement3));
        assertEquals(lStatement1.hashCode(), lStatement3.hashCode());

        assertTrue(!lStatement1.equals(lStatement2));
        assertTrue(lStatement1.hashCode() != lStatement2.hashCode());
    }

    @Test
    void testDelete() throws Exception {
        final String lExpectedName1 = "Test_Name_1";
        final String lExpectedName2 = "Test_Name_2";

        DataHouseKeeper.INSTANCE.createTestEntry(lExpectedName1);
        DataHouseKeeper.INSTANCE.createTestEntry(lExpectedName2);

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(2, lHome.getCount());

        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lExpectedName1);

        final String lDelete = "DELETE FROM TBLTEST WHERE TBLTEST.SNAME='Test_Name_1'";
        final QueryStatement lStatement = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        lStatement.setSQLString(lDelete);

        assertEquals(1, lStatement.executeUpdate(true));
        assertEquals(1, lHome.getCount());

        assertEquals(lExpectedName2, lHome.select().next().get("Name"));
    }

    @Test
    void testSerialization() throws SQLException, IOException, ClassNotFoundException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        QueryStatement lStatement = new DefaultQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
        final String lSQL = "SELECT SNAME FROM tblTest ORDER BY sName";
        lStatement.executeQuery(lSQL);

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lStatement);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lStatement = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final QueryStatement lRetrieved = (QueryStatement)lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        assertEquals(lSQL, lRetrieved.getSQLString());
    }

    @Test
    void testShowTables() throws Exception {
        final QueryStatement lStatement = new DefaultStatement();
        final Collection<String> lTables = lStatement.showTables();
        assertTrue(lTables.size() >= 6);

        final Collection<String> lTablesLower = new ArrayList<>();
        for (final String lTable : lTables) {
            lTablesLower.add(lTable.toLowerCase());
        }
        assertTrue(lTablesLower.contains("tblblobtest"));
        assertTrue(lTablesLower.contains("tblgroup"));
        assertTrue(lTablesLower.contains("tblgroupadmin"));
        assertTrue(lTablesLower.contains("tblparticipant"));
        assertTrue(lTablesLower.contains("tbltest"));
        assertTrue(lTablesLower.contains("tbltestshort"));
    }
}
