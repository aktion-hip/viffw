package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.PreparedInsertStatement;
import org.hip.kernel.bom.impl.PreparedUpdateStatement;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class PreparedStatementTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testPreparedInsert() throws VException, SQLException {
        final String lName1 = "Dummy";
        final String lName2 = "Pfiff";
        final Timestamp lTime1 = new Timestamp(Date.valueOf("1989-10-21").getTime());
        final String lFirstName1 = "Fi";
        final String lFirstName2 = "Adam";
        final Timestamp lTime2 = new Timestamp(Date.valueOf("2000-06-17").getTime());
        assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

        final DomainObject lBOM1 = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lBOM1.set("Name", lName1);
        lBOM1.set("Firstname", lFirstName1);
        lBOM1.set("Sex", new Integer(1));
        lBOM1.set("Mutation", lTime1);
        final DomainObject lBOM2 = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lBOM2.set("Name", lName2);
        lBOM2.set("Firstname", lFirstName2);
        lBOM2.set("Sex", new Integer(1));
        lBOM2.set("Amount", new BigDecimal(17.556));
        lBOM2.set("Mutation", lTime2);

        //insert
        final PreparedInsertStatement lStatement = new PreparedInsertStatement(
                DataHouseKeeper.INSTANCE.getSimpleHome());
        lStatement.setValues(lBOM1);
        Collection<Long> lAutoKeys = lStatement.executeUpdate();
        lStatement.commit();
        assertEquals(1, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
        assertEquals(1, countAutoKeys(lAutoKeys));
        lStatement.setValues(lBOM2);
        lAutoKeys = lStatement.executeUpdate();
        lStatement.commit();
        assertEquals(2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
        assertEquals(1, countAutoKeys(lAutoKeys));
        lStatement.close();

        //retrieve inserted
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName1);
        lKey.setValue("Firstname", lFirstName1);
        DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
        Timestamp lTimeRetrieved = (Timestamp)lFound.get("Mutation");
        assertEquals(lTime1.getTime(), lTimeRetrieved.getTime());

        lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName2);
        lKey.setValue("Firstname", lFirstName2);
        lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
        lTimeRetrieved = (Timestamp)lFound.get("Mutation");
        assertEquals(lTime2.getTime(), lTimeRetrieved.getTime());
    }

    @Test
    public void testPreparedUpdateSingle() throws VException, SQLException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(0, lHome.getCount());

        final String lWhereStreet = "Street-One";
        final String lWhereCity = "This";

        //first create three entries
        final PreparedInsertStatement lStatement = new PreparedInsertStatement(
                DataHouseKeeper.INSTANCE.getSimpleHome());
        DomainObject lModel = lHome.create();
        insertEntry(lStatement, "1", "Main Street", lWhereCity, "8090", lModel);
        insertEntry(lStatement, "2", lWhereStreet, lWhereCity, "8090", lModel);
        insertEntry(lStatement, "3", "Dorfstrasse", lWhereCity, "8090", lModel);
        assertEquals(3, lHome.getCount());

        final KeyObject lFind = new KeyObjectImpl();
        lFind.setValue("Street", lWhereStreet);
        lFind.setValue("City", lWhereCity);

        lModel = lHome.findByKey(lFind);
        assertNotNull(lModel);

        final String lNewName = "";
        final String lNewStreet = "";
        final String lNewCity = "";
        final String lNewPLZ = "";

        final KeyObject lTest = new KeyObjectImpl();
        lTest.setValue("Name", lNewName);
        lTest.setValue("Street", lNewStreet);
        lTest.setValue("City", lNewCity);
        lTest.setValue("PLZ", lNewPLZ);

        assertEquals(0, lHome.getCount(lTest));

        final PreparedUpdateStatement lUpdate = new PreparedUpdateStatement(lHome);
        lModel.set("Name", lNewName);
        lModel.set("Street", lNewStreet);
        lModel.set("City", lNewCity);
        lModel.set("PLZ", lNewPLZ);

        lUpdate.setValues(lModel);
        final int lChanged = lUpdate.executeUpdate();
        lUpdate.commit();

        assertEquals(1, lChanged);
        assertEquals(1, lHome.getCount(lTest));
    }

    @Test
    public void testPreparedUpdate() throws VException, SQLException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(0, lHome.getCount());

        final String lWhereStreet = "Street-One";
        final String lWhereCity = "This";

        //first create three entries
        final PreparedInsertStatement lStatement = new PreparedInsertStatement(
                DataHouseKeeper.INSTANCE.getSimpleHome());
        final DomainObject lModel = lHome.create();
        insertEntry(lStatement, "1", lWhereStreet, lWhereCity, "8090", lModel);
        insertEntry(lStatement, "2", "Main-Street", lWhereCity, "8090", lModel);
        insertEntry(lStatement, "3", lWhereStreet, lWhereCity, "8090", lModel);
        assertEquals(3, lHome.getCount());

        final String lNewPlz = "9000";
        final String lNewName = "Doe";
        KeyObject lChange = new KeyObjectImpl();
        lChange.setValue("PLZ", lNewPlz);
        lChange.setValue("Name", lNewName);

        final KeyObject lWhere = new KeyObjectImpl();
        lWhere.setValue("Street", lWhereStreet);
        lWhere.setValue("City", lWhereCity);

        assertEquals(0, lHome.getCount(lChange));

        final PreparedUpdateStatement lUpdate = new PreparedUpdateStatement(lHome, lChange, lWhere);
        final int lChanged = lUpdate.executeUpdate();
        lUpdate.commit();

        assertEquals(2, lChanged);
        lChange = new KeyObjectImpl();
        lChange.setValue("PLZ", lNewPlz);
        lChange.setValue("Name", lNewName);
        assertEquals(2, lHome.getCount(lChange));
    }

    private void insertEntry(final PreparedInsertStatement inStatement, final String inName, final String inStreet, final String inCity, final String inPLZ, final DomainObject inModel) throws VException, SQLException {
        inModel.setVirgin();
        inModel.set("Name", inName);
        inModel.set("Street", inStreet);
        inModel.set("City", inCity);
        inModel.set("PLZ", inPLZ);
        inModel.set("Sex", new Integer(1));
        inStatement.setValues(inModel);
        inStatement.executeUpdate();
        inStatement.commit();
    }

    private int countAutoKeys(final Collection<Long> inKeys) {
        int outNumber = 0;
        for (final Long lKey : inKeys) {
            System.out.println(lKey.toString());
            outNumber++;
        }
        return outNumber;
    }
}
