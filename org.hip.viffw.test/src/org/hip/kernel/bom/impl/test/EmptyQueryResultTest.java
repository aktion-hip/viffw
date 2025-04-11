package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.DomainObjectIterator;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.EmptyQueryResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * Created on 10.08.2003
 * @author Luthiger
 */
public class EmptyQueryResultTest {
    private DomainObjectHome lHome;

    @BeforeEach
    public void setUp() throws Exception {
        this.lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
    }

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    public void testClose() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            lResult.close();
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetCurrent() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNull(lResult.getCurrent());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetCurrentPage() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNull(lResult.getCurrentPage());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetKey() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNotNull(lResult.getKey());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testHasMoreElements() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertFalse(lResult.hasMoreElements());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNext() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNull(lResult.next());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextAsXMLString() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNull(lResult.nextAsXMLString());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextn() {
        try {
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertNotNull(lResult.nextn(10));
            for (final DomainObjectIterator lIterator = lResult.nextn(10).elements(); lIterator.hasMoreElements();) {
                lIterator.nextElement();
                fail("Should'nt get here.");
            }
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextnAsXMLString() {
        try {
            final String lNL = System.getProperty("line.separator");
            final String lExpected = lNL + "<ObjectList>" + lNL + "</ObjectList>";
            final QueryResult lResult = new EmptyQueryResult(this.lHome);
            assertEquals(lExpected, lResult.nextnAsXMLString(10));
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }
}
