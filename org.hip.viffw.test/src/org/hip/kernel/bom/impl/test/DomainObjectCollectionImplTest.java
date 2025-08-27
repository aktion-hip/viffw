package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.hip.kernel.bom.BOMNotFoundException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectCollection;
import org.hip.kernel.bom.DomainObjectIterator;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.DefaultQueryResult;
import org.hip.kernel.bom.impl.DomainObjectCollectionImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class DomainObjectCollectionImplTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws SQLException, NamingException, VException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        final String[] lFirstnames = {"Eva", "Pustekuchen", "Fi"};
        final String[] lFirstnames2 = {"Adam", "Egon", "Brum"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2], lFirstnames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1], lFirstnames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0], lFirstnames[0]);

        final ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        final QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult,
                null);
        final DomainObjectCollection lCollection = new DomainObjectCollectionImpl(lQueryResult);

        DomainObject lDomainObject;
        DomainObjectIterator lIterator = lCollection.elements();
        int i = 0;
        while (lIterator.hasMoreElements()) {
            lDomainObject = (DomainObject)lIterator.nextElement();
            assertEquals(lNames[i], lDomainObject.get("Name"));
            i++;
        }
        assertEquals(lNames.length, i);

        lIterator = lCollection.elements();
        i = 0;
        while (lIterator.hasMoreElements()) {
            lDomainObject = (DomainObject)lIterator.nextElement();
            lDomainObject.set("Firstname", lFirstnames2[i]);
            lDomainObject.update();
            lDomainObject.release();
            i++;
        }

        //test find
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name",lNames[0]);
        lKey.setValue("Firstname",lFirstnames[0]);

        //test find updated
        DomainObject lFound;
        try {
            lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            fail("Should not find object with Firstname=" + lFirstnames[0]);
        }
        catch(final BOMNotFoundException exc) {
            assertNotNull(exc);
        }

        lKey = new KeyObjectImpl();
        lKey.setValue("Name",lNames[0]);
        lKey.setValue("Firstname",lFirstnames2[0]);
        lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
    }

    @Test
    public void testSerialization() throws SQLException, NamingException, IOException, ClassNotFoundException, VException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        final String[] lFirstnames = {"Eva", "Pustekuchen", "Fi"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2], lFirstnames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1], lFirstnames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0], lFirstnames[0]);

        final ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        final QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult,
                null);
        DomainObjectCollection lCollection = new DomainObjectCollectionImpl(lQueryResult);

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lCollection);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lCollection = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final DomainObjectCollection lRetrieved = (DomainObjectCollection)lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        final DomainObjectIterator lIterator = lRetrieved.elements();
        int i = 0;
        while (lIterator.hasMoreElements()) {
            final DomainObject lDomainObject = (DomainObject)lIterator.nextElement();
            assertEquals(lNames[i], lDomainObject.get("Name"));
            i++;
        }
        assertEquals(lNames.length, i);
    }
}
