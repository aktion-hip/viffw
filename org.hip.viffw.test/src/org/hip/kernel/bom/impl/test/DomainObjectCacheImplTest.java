package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.hip.kernel.bom.BOMException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectCache;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.DomainObjectCacheImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class DomainObjectCacheImplTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws BOMException, SQLException, VException {
        final int lNumberBefore = DataHouseKeeper.INSTANCE.getSimpleHome().getCount();

        final String lName = "Dummy";
        final String lFirstname1 = "Fi";
        final String lFirstname2 = "Adam";
        DataHouseKeeper.INSTANCE.createTestEntry(lName, lFirstname1);
        DataHouseKeeper.INSTANCE.createTestEntry(lName, lFirstname2);
        assertEquals(lNumberBefore + 2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());


        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname1);
        final DomainObject lFound1 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        final KeyObject lID1 = lFound1.getKey();

        final DomainObjectCache lCache = new DomainObjectCacheImpl();
        lCache.put(lFound1);

        lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname2);
        final DomainObject lFound2 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        final KeyObject lID2 = lFound2.getKey();
        lCache.put(lFound2);

        assertTrue(lFound1 == lCache.get(lID1));
        assertTrue(lFound2 == lCache.get(lID2));
        assertTrue(!lFound1.equals(lCache.get(lID2)));
    }
}
