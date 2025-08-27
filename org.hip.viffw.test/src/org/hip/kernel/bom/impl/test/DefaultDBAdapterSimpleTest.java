package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.hip.kernel.bom.DBAdapterSimple;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.GroupByObject;
import org.hip.kernel.bom.HavingObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.LimitObject;
import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.impl.DBAdapterType;
import org.hip.kernel.bom.impl.DefaultDBAdapterSimple;
import org.hip.kernel.bom.impl.DomainObjectImpl;
import org.hip.kernel.bom.impl.GroupByObjectImpl;
import org.hip.kernel.bom.impl.HavingObjectImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.LimitObjectImpl;
import org.hip.kernel.bom.impl.ModifierStrategy;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** @author Luthiger Created: 15.10.2006 */
public class DefaultDBAdapterSimpleTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    void testCreateInsertString() {
        final String lExpected = "INSERT INTO tblTest( SFIRSTNAME, DTMUTATION, BSEX, FAMOUNT, SNAME, FDOUBLE ) VALUES ('Riese', TIMESTAMP('2002-02-01 10:00:00'), 1, 33, 'Adam', 1.2345678899999998900938180668163113296031951904296875 )";
        try {
            final DomainObject lObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
            lObject.set(Test2DomainObjectHomeImpl.KEY_NAME, "Adam");
            lObject.set(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Riese");
            lObject.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(1));
            lObject.set(Test2DomainObjectHomeImpl.KEY_AMOUNT, new BigDecimal(33));
            lObject.set(Test2DomainObjectHomeImpl.KEY_DOUBLE, new BigDecimal(1.23456789));

            final Calendar lCalender = Calendar.getInstance();
            lCalender.set(2002, 1, 1, 10, 0, 0);
            lCalender.getTime();
            lObject.set(Test2DomainObjectHomeImpl.KEY_MUTATION, new Timestamp(lCalender.getTimeInMillis()));

            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            final String lSQL = lAdapter.createInsertString("tblTest", lObject);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreateDeleteString() {
        final String lExpected1 = "DELETE FROM tblTest WHERE tblTest.TESTID = 12";
        final String lExpected2 = "DELETE FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            lObject.set("TestID", new BigDecimal(12));

            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            String lSQL = lAdapter.createDeleteString("tblTest", lObject);
            assertEquals(lExpected1, lSQL);

            lSQL = lAdapter.createDeleteString(createKey(), lHome);
            assertEquals(lExpected2, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreateUpdateString() throws SQLException, VException {
        final String lExpected = "UPDATE tblTest SET BSEX = 1, FAMOUNT = 33, SFIRSTNAME = 'Nova', DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') WHERE tblTest.TESTID = ";

        final Long lID = DataHouseKeeper.INSTANCE.createTestEntry("Testing");
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_ID, lID);
        final DomainObject lObject = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);

        final String lTestID = ((BigDecimal) lObject.get("TestID")).toString();

        lObject.set(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Nova");
        lObject.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(1));
        lObject.set(Test2DomainObjectHomeImpl.KEY_AMOUNT, new BigDecimal(33));

        final Calendar lCalender = Calendar.getInstance();
        lCalender.set(2002, 1, 1, 10, 0, 0);
        lCalender.getTime();
        lObject.set(Test2DomainObjectHomeImpl.KEY_MUTATION, new Timestamp(lCalender.getTimeInMillis()));

        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
        final String lSQL = lAdapter.createUpdateString("tblTest", lObject);
        assertEquals(lExpected + lTestID, lSQL);
    }

    @Test
    void testCreateUpdateString2() throws VException {
        final String lExpectedIns = "INSERT INTO tblTestShort( TESTID, SHORTID ) VALUES (77, 5 )";
        String lExpectedUpd = "UPDATE tblTestShort SET SHORTID = 6 WHERE tblTestShort.TESTID = 77 AND tblTestShort.SHORTID = 5";

        final DomainObject lObject = new TestShort();
        lObject.set(TestShortHome.KEY_SHORTID, Long.valueOf(5));
        lObject.set(TestShortHome.KEY_TESTID, Long.valueOf(77));
        ((DomainObjectImpl) lObject).reinitialize();

        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
        assertEquals(lExpectedIns, lAdapter.createInsertString("tblTestShort", lObject));

        // Note: ShortID is in the key. We set ShortID=6 for entry with ShortID=5 -> an update of an entry retrieved by
        // findByKey will work.
        lObject.set(TestShortHome.KEY_SHORTID, Long.valueOf(6));
        assertEquals(lExpectedUpd, lAdapter.createUpdateString("tblTestShort", lObject));

        // Note: Here we set ShortID=66 again for entry with ShortID=5 -> this will not work because the entry has
        // ShortID=6 now.
        // However, this shouldn't be a problem as we will have a findByKey before we update (and not several updates
        // immediately after another).
        lObject.set(TestShortHome.KEY_SHORTID, Long.valueOf(66));
        lExpectedUpd = "UPDATE tblTestShort SET SHORTID = 66 WHERE tblTestShort.TESTID = 77 AND tblTestShort.SHORTID = 5";
        assertEquals(lExpectedUpd, lAdapter.createUpdateString("tblTestShort", lObject));
    }

    @Test
    void testCreateUpdateString3() throws SQLException, VException {
        final String lExpected = "UPDATE tblTest SET tblTest.SSTREET = 'New 99', tblTest.SCITY = 'City 31' WHERE tblTest.TESTID = 23 AND tblTest.SNAME = 'Eva'";

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final DomainObject lObject = lHome.create();
        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());

        final KeyObject lValues = new KeyObjectImpl();
        lValues.setValue(Test2DomainObjectHomeImpl.KEY_STREET, "New 99");
        lValues.setValue(Test2DomainObjectHomeImpl.KEY_CITY, "City 31");

        final KeyObject lWhere = new KeyObjectImpl();
        lWhere.setValue(Test2DomainObjectHomeImpl.KEY_ID, Long.valueOf(23));
        lWhere.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "Eva");

        final String lSQL = lAdapter.createUpdateString(lHome, lValues, lWhere);
        assertEquals(lExpected, lSQL);
    }

    @Test
    void testCreatePreparedUpdateString() throws SQLException, VException {
        final String lExpected = "UPDATE tblTest SET BSEX = ?, FAMOUNT = ?, SFIRSTNAME = ?, DTMUTATION = ? WHERE tblTest.TESTID = ?";

        final Long lID = DataHouseKeeper.INSTANCE.createTestEntry("Testing");
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_ID, lID);
        final DomainObject lObject = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);

        lObject.set(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Seconda");
        lObject.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(0));
        lObject.set(Test2DomainObjectHomeImpl.KEY_AMOUNT, new BigDecimal(24));

        final Calendar lCalender = Calendar.getInstance();
        lCalender.set(2002, 1, 1, 10, 0, 0);
        lCalender.getTime();
        lObject.set(Test2DomainObjectHomeImpl.KEY_MUTATION, new Timestamp(lCalender.getTimeInMillis()));

        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
        final String lSQL = lAdapter.createPreparedUpdateString("tblTest", lObject);
        assertEquals(lExpected, lSQL);
    }

    @Test
    void testCreateCountAllString() {
        final String lExpected = "SELECT COUNT(tblTest.TESTID) FROM tblTest";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            final String lSQL = lAdapter.createCountAllString(lHome);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreateCountString() throws Exception {
        final String lExpected1 = "SELECT COUNT(tblTest.TESTID) FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";
        final String lExpected2 = "SELECT COUNT(tblTest.TESTID) FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') GROUP BY tblTest.FAMOUNT";
        final String lExpected3 = "SELECT DISTINCT COUNT(tblTest.TESTID) FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final DomainObject lObject = lHome.create();
        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());

        String lSQL = lAdapter.createCountString(createKey(), lHome);
        assertEquals(lExpected1, lSQL);

        final GroupByObject lGroupBy = new GroupByObjectImpl();
        lGroupBy.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, false, 0);
        lSQL = lAdapter.createCountString(createKey(), new HavingObjectImpl(), lGroupBy, lHome);
        assertEquals(lExpected2, lSQL);

        final KeyObject lKey = createKey();
        lKey.setDistinct(true);
        assertEquals(lExpected3, lAdapter.createCountString(lKey, lHome));
    }

    @Test
    void testCreateMaxAllString() {
        final String lExpected = "SELECT MAX(tblTest.FAMOUNT) FROM tblTest";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());

            final String lSQL = lAdapter.createModifiedString(new ModifierStrategy("Amount", ModifierStrategy.MAX),
                    lHome);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreateMaxString() throws VException {
        final String lExpected = "SELECT MAX(tblTest.FAMOUNT) FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";
        final String lExpected2 = "SELECT MAX(tblTest.FAMOUNT), MAX(tblTest.FDOUBLE) FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final DomainObject lObject = lHome.create();
        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());

        String lSQL = lAdapter.createModifiedString(new ModifierStrategy("Amount", ModifierStrategy.MAX), createKey(),
                lHome);
        assertEquals(lExpected, lSQL);

        final String[] lColumns = { Test2DomainObjectHomeImpl.KEY_AMOUNT, "Double" };
        lSQL = lAdapter.createModifiedString(new ModifierStrategy(lColumns, ModifierStrategy.MAX), createKey(), lHome);
        assertEquals(lExpected2, lSQL);
    }

    @Test
    void testCreateSelectAllString() {
        final String lExpected = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            final String lSQL = lAdapter.createSelectAllString();
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreateSelectString() throws Exception {
        final String lExpected1 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";
        final String lExpected2 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE (tblTest.SFIRSTNAME LIKE 'Test%' OR tblTest.SFIRSTNAME LIKE 'test%') AND tblTest.FAMOUNT > 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') ORDER BY tblTest.SFIRSTNAME, tblTest.FAMOUNT DESC";
        final String lExpected3 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest ORDER BY tblTest.SFIRSTNAME, tblTest.FAMOUNT DESC";
        final String lExpected4 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') AND UCASE(tblTest.SNAME) LIKE 'A%'";
        final String lExpected5 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE (tblTest.SFIRSTNAME LIKE 'Test%' OR tblTest.SFIRSTNAME LIKE 'test%') AND tblTest.FAMOUNT > 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') HAVING tblTest.FAMOUNT <= 10";
        final String lExpected6 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE (tblTest.SFIRSTNAME LIKE 'Test%' OR tblTest.SFIRSTNAME LIKE 'test%') AND tblTest.FAMOUNT > 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') GROUP BY tblTest.FAMOUNT HAVING tblTest.FAMOUNT <= 10";
        final String lExpected7 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest GROUP BY tblTest.FAMOUNT";
        final String lExpected8 = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00') LIMIT 10 OFFSET 60";
        final String lExpected9 = "SELECT DISTINCT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE tblTest.SFIRSTNAME = 'Seconda' AND tblTest.BSEX = 0 AND tblTest.FAMOUNT = 24 AND tblTest.DTMUTATION = TIMESTAMP('2002-02-01 10:00:00')";

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final DomainObject lObject = lHome.create();
        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
        String lSQL = lAdapter.createSelectString(createKey(), lHome);
        assertEquals(lExpected1, lSQL);

        final OrderObject lOrder = new OrderObjectImpl();
        lOrder.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, false, 1);
        lOrder.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, true, 2);
        lSQL = lAdapter.createSelectString(createKey2(), lOrder, lHome);
        assertEquals(lExpected2, lSQL);

        lSQL = lAdapter.createSelectString(lOrder, lHome);
        assertEquals(lExpected3, lSQL);

        if (DataHouseKeeper.INSTANCE.isDBMySQL()) {
            lSQL = lAdapter.createSelectString(createKey3(), lHome);
            assertEquals(lExpected4, lSQL);
        }

        final HavingObject lHaving = new HavingObjectImpl();
        lHaving.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, Integer.valueOf(10), "<=");
        lSQL = lAdapter.createSelectString(createKey2(), lHaving, lHome);
        assertEquals(lExpected5, lSQL);

        final GroupByObject lGroupBy = new GroupByObjectImpl();
        lGroupBy.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, false, 0);
        lSQL = lAdapter.createSelectString(createKey2(), new OrderObjectImpl(), lHaving, lGroupBy, lHome);
        assertEquals(lExpected6, lSQL);

        lSQL = lAdapter.createSelectString(new KeyObjectImpl(), new OrderObjectImpl(), new HavingObjectImpl(),
                lGroupBy, lHome);
        assertEquals(lExpected7, lSQL);

        final LimitObject lLimit = new LimitObjectImpl(10, 60);
        lSQL = lAdapter.createSelectString(createKey(), lLimit, lHome);
        assertEquals(lExpected8, lSQL);

        final KeyObject lKey = createKey();
        lKey.setDistinct(true);
        assertEquals(lExpected9, lAdapter.createSelectString(lKey, lHome));
    }

    @Test
    void testCreatePreparedSelectString() {
        final String lExpected = "SELECT tblTest.BSEX, tblTest.FAMOUNT, tblTest.SCITY, tblTest.FDOUBLE, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.TESTID, tblTest.SMAIL, tblTest.SLANGUAGE, tblTest.SSTREET, tblTest.STEL, tblTest.DTMUTATION, tblTest.SFAX, tblTest.SPLZ, tblTest.SPASSWORD FROM tblTest WHERE tblTest.SFIRSTNAME = ? AND tblTest.BSEX = ? AND tblTest.FAMOUNT = ? AND tblTest.DTMUTATION = ?";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            final String lSQL = lAdapter.createPreparedSelectString(createKey(), lHome);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreatePreparedUpdate() throws VException {
        final String lExpected = "UPDATE tblTest SET tblTest.SSTREET = ?, tblTest.SCITY = ? WHERE tblTest.TESTID = ? AND tblTest.SNAME = ?";

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final DomainObject lObject = lHome.create();
        final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());

        final KeyObject lValues = new KeyObjectImpl();
        lValues.setValue(Test2DomainObjectHomeImpl.KEY_STREET, "New 99");
        lValues.setValue(Test2DomainObjectHomeImpl.KEY_CITY, "City 31");

        final KeyObject lWhere = new KeyObjectImpl();
        lWhere.setValue(Test2DomainObjectHomeImpl.KEY_ID, Long.valueOf(23));
        lWhere.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "Eva");

        final String lSQL = lAdapter.createPreparedUpdate(lHome, lValues, lWhere);
        assertEquals(lExpected, lSQL);
    }

    @Test
    void testCreateKeyCountColumnList() {
        final String lExpected = "COUNT(tblTest.TESTID)";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            final String lSQL = lAdapter.createKeyCountColumnList(lHome);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreatePreparedInserts() {
        final String lExpected = "INSERT INTO tblTest( SLANGUAGE, SFIRSTNAME, DTMUTATION, SPLZ, SCITY, BSEX, SSTREET, FAMOUNT, TESTID, SNAME, FDOUBLE, SFAX, STEL, SPASSWORD, SMAIL ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            // String lSQL = (String)lAdapter.createPreparedInserts().elementAt(0);
            final String lSQL = lAdapter.createPreparedInserts2().get(0);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testCreatePreparedUpdates() {
        final String lExpected = "UPDATE tblTest SET SLANGUAGE = ?, SFIRSTNAME = ?, DTMUTATION = ?, SPLZ = ?, SCITY = ?, BSEX = ?, SSTREET = ?, FAMOUNT = ?, SNAME = ?, FDOUBLE = ?, SFAX = ?, STEL = ?, SPASSWORD = ?, SMAIL = ? WHERE TESTID = ?";
        try {
            final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
            final DomainObject lObject = lHome.create();
            final DBAdapterSimple lAdapter = new DefaultDBAdapterSimple(lObject.getObjectDef());
            // final String lSQL = lAdapter.createPreparedUpdates().elementAt(0);
            final String lSQL = lAdapter.createPreparedUpdates2().get(0);
            assertEquals(lExpected, lSQL);
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    // --- helper methods ---

    private KeyObject createKey() {
        final KeyObject outKey = new KeyObjectImpl();
        try {
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Seconda");
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(0));
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new BigDecimal(24));

            final Calendar lCalender = Calendar.getInstance();
            lCalender.set(2002, 1, 1, 10, 0, 0);
            lCalender.getTime();
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_MUTATION, new Timestamp(lCalender.getTimeInMillis()));
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
        return outKey;
    }

    private KeyObject createKey2() {
        final KeyObject outKey = new KeyObjectImpl();
        try {
            final KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Test%", "LIKE");
            lKey.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "test%", "LIKE", KeyObject.BinaryBooleanOperator.OR);
            outKey.setValue(lKey);
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new BigDecimal(24), ">");

            final Calendar lCalender = Calendar.getInstance();
            lCalender.set(2002, 1, 1, 10, 0, 0);
            lCalender.getTime();
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_MUTATION, new Timestamp(lCalender.getTimeInMillis()));
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
        return outKey;
    }

    private KeyObject createKey3() {
        KeyObject outKey = null;
        try {
            outKey = createKey();
            outKey.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "A%", "LIKE", KeyObject.BinaryBooleanOperator.AND,
                    DBAdapterType.DB_TYPE_MYSQL.getColumnModifierUCase());
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
        return outKey;
    }

    // --- private classes ---

    @SuppressWarnings("serial")
    private class TestShort extends DomainObjectImpl {
        private final static String HOME_CLASS_NAME = "org.hip.kernel.bom.impl.test.TestShortHome";

        @Override
        public String getHomeClassName() {
            return HOME_CLASS_NAME;
        }
    }

}
