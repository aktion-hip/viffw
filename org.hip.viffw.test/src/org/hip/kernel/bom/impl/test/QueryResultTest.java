package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;

import org.hip.kernel.bom.AlternativeModel;
import org.hip.kernel.bom.AlternativeModelFactory;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.AlternativeQueryResult;
import org.hip.kernel.bom.impl.DefaultQueryResult;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class QueryResultTest {
    private final static String EXPECTED1 = "<Name>1 eins</Name>";
    private final static String EXPECTED2 = "<Name>2 zwei</Name>";
    private final static String EXPECTED3 = "<Name>3 drei</Name>";


    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    void testDo() throws SQLException, NamingException, VException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        final ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT tblTest.SNAME FROM tblTest ORDER BY sName");
        final QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult,
                null);
        assertTrue(lQueryResult.hasMoreElements());

        GeneralDomainObject lDomainObject;
        int i = 0;
        while (lQueryResult.hasMoreElements()) {
            lDomainObject = lQueryResult.nextAsDomainObject();
            assertEquals(lNames[i], lDomainObject.get("Name"));
            i++;
        }
        assertEquals(lNames.length, i);
    }

    @Test
    void testLoad() throws Exception {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        ResultSet result = DataHouseKeeper.INSTANCE.executeQuery("SELECT tblTest.SNAME FROM tblTest ORDER BY sName");
        QueryResult queryResult = new AlternativeQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), result, null,
                new TestModelFactory());
        if (queryResult instanceof final AlternativeQueryResult altResult) {
            final Collection<AlternativeModel> entries = altResult.getAlternativeModels();
            assertEquals(3, entries.size());
            int i = 0;
            for (final AlternativeModel lAlternativeModel : entries) {
                assertEquals(lNames[i++], ((TestModel) lAlternativeModel).name);
            }
        } else {
            fail("Unexpected result");
        }

        // test limit
        final int expectedSize = 2;
        result = DataHouseKeeper.INSTANCE.executeQuery("SELECT tblTest.SNAME FROM tblTest ORDER BY sName");
        queryResult = new AlternativeQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), result, null,
                new TestModelFactory());
        if (queryResult instanceof final AlternativeQueryResult altResult) {
            final List<AlternativeModel> entries = altResult.getAlternativeModels(expectedSize);
            assertEquals(expectedSize, entries.size());
            int i = 0;
            for (final AlternativeModel lAlternativeModel : entries) {
                assertEquals(lNames[i++], ((TestModel) lAlternativeModel).name);
            }
        } else {
            fail("Unexpected result");
        }
    }

    @Test
    void testSerialize() throws SQLException, NamingException, VException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        final String lSerializerName = "org.hip.kernel.bom.impl.XMLSerializer";

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult, null);
        String lXML = lQueryResult.nextAsXMLString();
        assertTrue(lXML.indexOf(EXPECTED1) > -1);

        lResult = DataHouseKeeper.INSTANCE.executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult, null);
        String lXML2 = lQueryResult.nextAsXMLString(lSerializerName);
        assertEquals(lXML, lXML2);

        lResult = DataHouseKeeper.INSTANCE.executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult, null);
        lXML = lQueryResult.nextnAsXMLString(3);
        final int lPosition1 = lXML.indexOf(EXPECTED1);
        final int lPosition2 = lXML.indexOf(EXPECTED2);
        final int lPosition3 = lXML.indexOf(EXPECTED3);
        assertTrue(lPosition1 > -1);
        assertTrue(lPosition2 > lPosition1);
        assertTrue(lPosition3 > lPosition2);

        lResult = DataHouseKeeper.INSTANCE.executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult, null);
        lXML2 = lQueryResult.nextnAsXMLString(3, lSerializerName);
        assertEquals(lXML, lXML2);
    }

    private QueryResult createQueryResult(final String[] inNames) throws SQLException, VException {
        DataHouseKeeper.INSTANCE.createTestEntry(inNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(inNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(inNames[0]);

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(3, lHome.getCount());

        final OrderObject lOrder = new OrderObjectImpl();
        lOrder.setValue(Test2DomainObjectHomeImpl.KEY_NAME, 1);
        return lHome.select(lOrder);
    }

    //	--- private classes ---

    private class TestModelFactory implements AlternativeModelFactory {

        @Override
        public AlternativeModel createModel(final ResultSet inResultSet) throws SQLException {
            return new TestModel(inResultSet.getString(1));
        }

    }

    private class TestModel implements AlternativeModel {
        public String name;

        TestModel(final String inName) {
            this.name = inName;
        }
    }

}
