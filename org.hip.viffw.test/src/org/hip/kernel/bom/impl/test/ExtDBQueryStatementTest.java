package org.hip.kernel.bom.impl.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 * Created: 18.04.2007
 */
public class ExtDBQueryStatementTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    /**
     * Note: needs OSGi running, because we need to register a DataSourceFactory to the DataSourceRegistery.
     * @throws Exception
     */
    @Test
    @Disabled("OSGi needed")
    public void testDo() throws Exception {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei"};
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        //		DataSourceRegistry.INSTANCE.register(null);

        // final QueryStatement statement = new ExtDBQueryStatement(DataHouseKeeper.INSTANCE.getSimpleHome(),
        // DataHouseKeeper.INSTANCE.getDBAccessConfiguration());
        // final QueryResult result = statement.executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY
        // sName");
        // int i = 0;
        // while (result.hasMoreElements()) {
        // final GeneralDomainObject lModel = result.next();
        // assertEquals(lNames[i++], lModel.get("Name"));
        // }
    }

}
