package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Testing security issues (code injection etc.).
 *
 * @author Luthiger
 * Created: 22.07.2007
 * @see http://www.owasp.org/index.php/OWASP_Top_Ten_Project
 */
public class SecurityTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    public void testCodeInjectionInsert() throws VException, SQLException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        assertEquals(0, lHome.getCount());

        //create new domain objects and insert them
        //normal
        DomainObject lDomainObject = lHome.create();
        lDomainObject.set("Name", "Doe");
        lDomainObject.set("Firstname", "Jane 1");
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(1, lHome.getCount());

        //having apostrophe included
        lDomainObject = lHome.create();
        lDomainObject.set("Name", "Doe 'test");
        lDomainObject.set("Firstname", "Jane 2");
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(2, lHome.getCount());

        //having quotation mark included
        lDomainObject = lHome.create();
        lDomainObject.set("Name", "Doe \"test");
        lDomainObject.set("Firstname", "Jane 3");
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(3, lHome.getCount());

        //various selects
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Firstname", "Jane 3");
        assertEquals(1, process(lHome.select(lKey)));

        lKey = new KeyObjectImpl();
        lKey.setValue("Firstname", "Jane 3'-");
        assertEquals(0, process(lHome.select(lKey)));

        lKey = new KeyObjectImpl();
        lKey.setValue("Firstname", "'DELETE tblTest -");
        assertEquals(0, process(lHome.select(lKey)));

        assertEquals(3, process(lHome.select()));
        assertEquals(3, lHome.getCount());
    }

    private int process(final QueryResult inResult) throws VException, SQLException {
        int outCount = 0;
        while (inResult.hasMoreElements()) {
            outCount++;
            inResult.next();
        }
        return outCount;
    }

}
