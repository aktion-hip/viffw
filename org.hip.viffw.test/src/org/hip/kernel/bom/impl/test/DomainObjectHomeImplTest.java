package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.SetOperatorHome;
import org.hip.kernel.bom.SettingException;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.ModifierStrategy;
import org.hip.kernel.bom.impl.SetOperatorHomeImpl;
import org.hip.kernel.bom.impl.XMLSerializer;
import org.hip.kernel.bom.model.MappingDef;
import org.hip.kernel.bom.model.ObjectDef;
import org.hip.kernel.bom.model.PropertyDef;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class DomainObjectHomeImplTest {
    private final static String KEY_NAME = "Name";
    private static TestDomainObjectHomeImpl home;

    private class UnionHomeSub extends SetOperatorHomeImpl {
        private UnionHomeSub() {
            super(SetOperatorHome.UNION);
        }

        public String getSQL() {
            return createSelect();
        }
    }

    @BeforeAll
    public static void init() {
        home = (TestDomainObjectHomeImpl) VSys.homeManager
                .getHome("org.hip.kernel.bom.impl.test.TestDomainObjectHomeImpl");
    }

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    private String createColumnList(final ObjectDef inObjectDef) {
        String outColumnList = "";

        outColumnList = "";
        try {
            // We iterate over all mapping entries
            boolean lFirst = true;
            for (final PropertyDef lProperty : inObjectDef.getPropertyDefs2()) {
                final MappingDef lMapping = lProperty.getMappingDef();

                if (!lFirst) {
                    outColumnList += ", ";
                }
                lFirst = false;

                final String lTableName = lMapping.getTableName();
                final String lColumnName = lMapping.getColumnName();

                outColumnList += lTableName + "." + lColumnName;
            }
        } catch (final Exception exc) {
            fail(exc.getMessage());
        }
        return outColumnList;
    }

    @Test
    void testCreate() {
        try {
            int lCount = DataHouseKeeper.INSTANCE.getSimpleHome().getCount();
            assertEquals(0, lCount);

            final String lName = "Dummy";
            final String lFirstname = "Fi";

            // test create / insert
            DataHouseKeeper.INSTANCE.createTestEntry(lName, lFirstname);
            lCount = DataHouseKeeper.INSTANCE.getSimpleHome().getCount();
            assertEquals(1, lCount);

            // test find / delete
            final KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_NAME, lName);
            lKey.setValue("Firstname", lFirstname);

            DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            final XMLSerializer lXML = new XMLSerializer();
            lFound.accept(lXML);
            VSys.out.print(lXML.toString());
            assertTrue(lXML.toString().length() > 400);
            lFound.delete();

            lCount = DataHouseKeeper.INSTANCE.getSimpleHome().getCount();
            assertEquals(0, lCount);

            // insert with double amount
            final BigDecimal lAmount = new BigDecimal(17.556);
            final DomainObject lNew = DataHouseKeeper.INSTANCE.getSimpleHome().create();
            lNew.set(KEY_NAME, lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Mail", "dummy1@aktion-hip.ch");
            lNew.set("Sex", Integer.valueOf(1));
            lNew.set("Amount", lAmount);
            lNew.insert(true);

            lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            final BigDecimal lReturned = (BigDecimal) lFound.get("Amount");
            assertEquals(lAmount.setScale(3, RoundingMode.DOWN), lReturned);
            lFound.delete();
        } catch (final org.hip.kernel.exc.VException exc) {
            fail("testCreate f1 " + exc.getMessage());
        } catch (final java.sql.SQLException exc) {
            fail("testCreate f2 " + exc.getMessage());
        }
    }

    @Test
    public void testStrings() throws VException, SQLException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(0, lHome.getCount());
        final DomainObject lModel = lHome.create();

        String lText = "String with apostroph: Here\'s one (1).";
        lModel.set(Test2DomainObjectHomeImpl.KEY_NAME, lText);
        lModel.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(1));
        final Long lID = lModel.insert(true);
        assertEquals(1, lHome.getCount());

        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_ID, lID);
        DomainObject lRetrieved = lHome.findByKey(lKey);
        assertEquals("String with apostroph: Here's one (1).",
                lRetrieved.get(Test2DomainObjectHomeImpl.KEY_NAME).toString());

        lText = "String with apostroph: Here's one (2).";
        lRetrieved.set(Test2DomainObjectHomeImpl.KEY_NAME, lText);
        lRetrieved.update(true);
        lRetrieved = lHome.findByKey(lKey);
        assertEquals(lText, lRetrieved.get(Test2DomainObjectHomeImpl.KEY_NAME).toString());

        lText = "This is a quote: \"Hello World\"";
        lRetrieved.set(Test2DomainObjectHomeImpl.KEY_NAME, lText);
        lRetrieved.update(true);
        lRetrieved = lHome.findByKey(lKey);
        assertEquals(lText, lRetrieved.get(Test2DomainObjectHomeImpl.KEY_NAME).toString());
    }

    @Test
    public void testEquals() {
        try {
            final String lName = "Dummy";
            final String lFirstname = "1";
            final String lName2 = "Dummy";
            final String lFirstname2 = "Eva";

            // test create / insert
            final DomainObject lNew = DataHouseKeeper.INSTANCE.getSimpleHome().create();
            lNew.set(KEY_NAME, lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Mail", "dummy1@aktion-hip.ch");
            lNew.set("Sex", Integer.valueOf(1));

            final DomainObject lNew2 = DataHouseKeeper.INSTANCE.getSimpleHome().create();
            lNew2.set(KEY_NAME, lName2);
            lNew2.set("Firstname", lFirstname2);
            lNew2.set("Mail", "dummy2@aktion-hip.ch");
            lNew2.set("Sex", Integer.valueOf(2));

            assertTrue(lNew2.equals(lNew));
            assertEquals(lNew2.hashCode(), lNew.hashCode());

            lNew.insert();
            lNew2.insert();

            final KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_NAME, lName);
            lKey.setValue("Firstname", lFirstname);
            final DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);

            final KeyObject lKey2 = new KeyObjectImpl();
            lKey2.setValue(KEY_NAME, lName2);
            lKey2.setValue("Firstname", lFirstname2);
            final DomainObject lFound2 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey2);

            if (lFound.equals(lFound2)) {
                fail("Found domain objects arn't equal anymore!");
            }

            if (lFound.hashCode() == lFound2.hashCode()) {
                fail("Found domain objects must return different hashCode!");
            }

            lFound.delete();
            lFound2.delete();
        } catch (final org.hip.kernel.exc.VException exc) {
            fail("testCreate f1 " + exc.getMessage());
        } catch (final java.sql.SQLException exc) {
            fail("testCreate f2 " + exc.getMessage());
        }
    }

    @Test
    public void testGetColumnNameFor() {
        assertEquals("tblTestMember.TESTMEMBERID", home.getColumnNameFor("MemberID"));
        assertEquals("tblTestMember.DTMUTATION", home.getColumnNameFor("Mutation"));
        assertEquals("", home.getColumnNameFor("Dummy"));
    }

    @Test
    public void testGetObjectDef() {
        final ObjectDef lDef = home.getObjectDef();
        try {
            lDef.get("keyDefs");
            lDef.get("propertyDefs");
            assertEquals("org.hip.kernel.bom.DomainObject", lDef.get("parent"));
            assertEquals("TestDomainObject", lDef.get("objectName"));
        } catch (final org.hip.kernel.bom.GettingException exc) {
            fail("testGetObjectDef.get()");
        }
    }

    @Test
    public void testGetPropertyDef() {
        final String[] lExpected = { "propertyType", "valueType", "propertyName", "mappingDef", "formatPattern",
        "relationshipDef" };
        final Vector<String> lContainsExpected = new Vector<String>(Arrays.asList(lExpected));

        final PropertyDef lDef = home.getPropertyDef("MemberID");
        assertEquals("simple", lDef.getPropertyType());
        assertEquals("Number", lDef.getValueType());
        for (final String lName : lDef.getPropertyNames2()) {
            assertTrue(lContainsExpected.contains(lName));
        }
        assertEquals("tblTestMember.TESTMEMBERID", lDef.getMappingDef().getTableName() + "."
                + lDef.getMappingDef().getColumnName());
        assertNull(lDef.getRelationshipDef());
    }

    @Test
    public void testGetPropertyDefFor() {
        final String[] lExpected = { "propertyType", "valueType", "propertyName", "mappingDef", "formatPattern",
        "relationshipDef" };
        final List<String> lContainsExpected = new ArrayList<>(Arrays.asList(lExpected));

        final PropertyDef lDef = home.getPropertyDefFor("DTMUTATION");
        assertEquals("simple", lDef.getPropertyType());
        assertEquals("Timestamp", lDef.getValueType());
        assertEquals("Mutation", lDef.getName());
        for (final String lName : lDef.getPropertyNames2()) {
            assertTrue(lContainsExpected.contains(lName));
        }
        assertEquals("tblTestMember.DTMUTATION", lDef.getMappingDef().getTableName() + "."
                + lDef.getMappingDef().getColumnName());
        assertNull(lDef.getRelationshipDef());
    }

    @Test
    public void testObjects() {
        final String[] lExpectedCols = { "tblTestMember.TESTMEMBERID", "tblTestMember.SNAME",
                "tblTestMember.SPASSWORD", "tblTestMember.SFIRSTNAME", "tblTestMember.DTMUTATION" };
        final Vector<String> lVExpectedCols = new Vector<String>(Arrays.asList(lExpectedCols));

        // ordering in ColumnList may be OS-dependent, so get the actual ColumnList
        final String lColumnList = createColumnList(home.getObjectDef());

        // check whether the actual ColumnList contains the expected items
        final StringTokenizer lStringTokenizer = new StringTokenizer(lColumnList, ",");
        int i = 0;
        while (lStringTokenizer.hasMoreTokens()) {
            i++;
            assertTrue(lVExpectedCols.contains(lStringTokenizer.nextToken().trim()));
        }
        assertEquals(lVExpectedCols.size(), i);

        // check the other test objects with the help of the actual ColumnList
        final String[] lExpected = new String[6];
        lExpected[0] = "SELECT COUNT(tblTestMember.TESTMEMBERID) FROM tblTestMember";
        lExpected[1] = "SELECT COUNT(tblTestMember.TESTMEMBERID) FROM tblTestMember WHERE tblTestMember.SNAME = 'Luthiger' AND tblTestMember.TESTMEMBERID = 12 AND tblTestMember.DTMUTATION = TIMESTAMP('2001-12-24 00:00:00')";
        lExpected[2] = "COUNT(tblTestMember.TESTMEMBERID)";
        lExpected[3] = "SELECT " + lColumnList
                + " FROM tblTestMember WHERE tblTestMember.SNAME = ? AND tblTestMember.TESTMEMBERID = ?";
        lExpected[4] = "SELECT " + lColumnList + " FROM tblTestMember";
        lExpected[5] = "SELECT "
                + lColumnList
                + " FROM tblTestMember WHERE tblTestMember.SNAME = 'Luthiger' AND tblTestMember.TESTMEMBERID = 12 AND tblTestMember.DTMUTATION = TIMESTAMP('2001-12-24 00:00:00')";

        Iterator<Object> lTestObjects = home.getTestObjects();
        assertNotNull(lTestObjects);

        i = 0;
        for (lTestObjects = home.getTestObjects(); lTestObjects.hasNext();) {
            assertEquals(lExpected[i], lTestObjects.next());
            i++;
        }
    }

    @Test
    public void testSetCheck() {

        final String lName = "Dummy";
        final String lFirstname = "Fi";
        DomainObject lNew = null;

        try {
            lNew = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        } catch (final org.hip.kernel.bom.BOMException exc) {
            fail("testSetCheck create " + exc.getMessage());
        }

        try {
            lNew.set(KEY_NAME, lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Double", Float.valueOf((float) 13.11));
        } catch (final org.hip.kernel.bom.SettingException exc) {
            fail("testSetCheck set " + exc.getMessage());
        }

        try {
            lNew.set("Double", "13.11");
            fail("testSetCheck set with false type");
        } catch (final SettingException exc) {
        }
    }

    @Test
    public void testDelete() throws Exception {
        final String lExpectedName1 = "Test_Name_1";
        final String lExpectedName2 = "Test_Name_2";

        DataHouseKeeper.INSTANCE.createTestEntry(lExpectedName1);
        DataHouseKeeper.INSTANCE.createTestEntry(lExpectedName2);

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(2, lHome.getCount());

        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(KEY_NAME, lExpectedName1);

        lHome.delete(lKey, true);
        assertEquals(1, lHome.getCount());

        assertEquals(lExpectedName2, lHome.select().next().get(KEY_NAME));
    }

    @Test
    void testMax() throws VException, SQLException {
        final double[] lExpected = { 12.45, 112.331967 };

        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(0, lHome.getCount());

        DomainObject lNew = lHome.create();
        lNew.set("Name", "Test1");
        lNew.set("Firstname", "1");
        lNew.set("Mail", "dummy1@aktion-hip.ch");
        lNew.set("Sex", Integer.valueOf(1));
        lNew.set("Amount", Float.valueOf((float) lExpected[0]));
        lNew.set("Double", Float.valueOf((float) 13.11));
        lNew.insert(true);
        lNew.release();

        lNew = lHome.create();
        lNew.set("Name", "Test2");
        lNew.set("Firstname", "2");
        lNew.set("Mail", "dummy2@aktion-hip.ch");
        lNew.set("Sex", Integer.valueOf(1));
        lNew.set("Amount", Float.valueOf(10));
        lNew.set("Double", Float.valueOf((float) lExpected[1]));
        lNew.insert(true);
        lNew.release();

        lNew = lHome.create();
        lNew.set("Name", "Test3");
        lNew.set("Firstname", "3");
        lNew.set("Mail", "dummy3@aktion-hip.ch");
        lNew.set("Sex", Integer.valueOf(1));
        lNew.set("Amount", Float.valueOf((float) 5.4451));
        lNew.set("Double", Float.valueOf((float) 89.4));
        lNew.insert(true);
        lNew.release();

        assertEquals(3, lHome.getCount());
        assertEquals(lExpected[0], lHome.getMax("Amount").doubleValue(), 0.0001);
        assertEquals(lExpected[1], lHome.getMax("Double").doubleValue(), 0.0001);

        final String[] lColumns = { "Amount", "Double" };
        final Collection<Object> lResult = lHome.getModified(new ModifierStrategy(lColumns, ModifierStrategy.MAX));
        final Iterator<Object> lValues = lResult.iterator();
        for (int i = 0; i < lExpected.length; i++) {
            assertEquals(lExpected[i], ((BigDecimal) lValues.next()).doubleValue(),
                    0.0001);
        }
    }

    @Test
    void testCreateSelectUnion() throws VException {
        final String lExpectedName = "Test_Name";
        final String lExpected = "(SELECT tblTestMember.TESTMEMBERID, tblTestMember.SFIRSTNAME, tblTestMember.DTMUTATION, tblTestMember.SNAME, tblTestMember.SPASSWORD FROM tblTestMember WHERE tblTestMember.SNAME = 'Test_Name')";
        final UnionHomeSub lUnionHome = new UnionHomeSub();

        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(KEY_NAME, lExpectedName);

        home.createSelectString(lUnionHome, lKey);
        assertEquals(lExpected, lUnionHome.getSQL());
    }

    @Test
    void testSerialization() throws IOException, ClassNotFoundException, VException, SQLException {
        DomainObjectHome lHome = (Test2DomainObjectHomeImpl) VSys.homeManager
                .getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");
        assertEquals(0, lHome.getCount());
        DomainObject lNew = lHome.create();
        lNew.set(Test2DomainObjectHomeImpl.KEY_NAME, "Test 1");
        lNew.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(1));
        lNew.insert(true);
        assertEquals(1, lHome.getCount());

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lHome);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lHome = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final DomainObjectHome lRetrieved = (DomainObjectHome) lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        assertEquals(1, lRetrieved.getCount());
        lNew = lRetrieved.create();
        lNew.set(Test2DomainObjectHomeImpl.KEY_NAME, "Test 2");
        lNew.set(Test2DomainObjectHomeImpl.KEY_SEX, Integer.valueOf(0));
        lNew.insert(true);
        assertEquals(2, lRetrieved.getCount());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSerialization2() throws IOException, ClassNotFoundException {
        DomainObjectHome lHome1 = (Test2DomainObjectHomeImpl) VSys.homeManager
                .getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");
        DomainObjectHome lHome2 = (Test2DomainObjectHomeImpl) VSys.homeManager
                .getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");
        assertTrue(lHome1 == lHome2);

        Collection<DomainObjectHome> lHomes = new Vector<DomainObjectHome>(2);
        lHomes.add(lHome1);
        lHomes.add(lHome2);
        assertEquals(2, lHomes.size());

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lHomes);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lHomes = null;
        lHome1 = null;
        lHome2 = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final Vector<DomainObjectHome> lRetrieved = (Vector<DomainObjectHome>) lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        assertEquals(2, lRetrieved.size());
        assertTrue(lRetrieved.elementAt(0) == lRetrieved.elementAt(1));

    }

}
