package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.Vector;

import org.hip.kernel.bom.BOMNotFoundException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.UpdateStatement;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class UpdateStatementTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws SQLException, VException {
        final String lName = "Dummy";
        final String lFirstName = "Fi";
        final String lFirstName2 = "Adam";
        assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
        DataHouseKeeper.INSTANCE.createTestEntry(lName, lFirstName);
        DataHouseKeeper.INSTANCE.createTestEntry("Adam", "Pfiff");
        assertEquals(2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstName);
        DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        final int lKeyValue = ((Number) lFound.get("TestID")).intValue();

        final String lSQL = "UPDATE TBLTEST SET SFIRSTNAME = '" + lFirstName2 + "' WHERE TESTID=" + lKeyValue;
        final Vector<String> lUpdates = new Vector<String>();
        lUpdates.addElement(lSQL);

        final UpdateStatement lStatement = new UpdateStatement();
        lStatement.setUpdates(lUpdates);
        lStatement.executeUpdate();
        lStatement.close();

        // test find updated
        try {
            lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            fail("Should not find object with Firstname=" + lFirstName);
        } catch (final BOMNotFoundException exc) {
        }

        lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstName2);
        lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
    }
}
