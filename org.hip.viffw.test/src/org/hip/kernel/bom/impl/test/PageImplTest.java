package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.Page;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.bom.impl.PageImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class PageImplTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws SQLException, NamingException, VException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei", "4 vier"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[3]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        final OrderObject lOrder = new OrderObjectImpl();
        lOrder.setValue(Test2DomainObjectHomeImpl.KEY_NAME, 1);
        Page lPage = new PageImpl(DataHouseKeeper.INSTANCE.getSimpleHome().select(lOrder), null, 3);

        final Page lFirst = lPage;
        final Page lSecond = lPage.getNextPage();
        assertTrue(lPage.getPreviousPage() == lPage);
        assertEquals(1, lPage.getPageNumber());
        assertEquals(2, lPage.getNextPage().getPageNumber());
        assertEquals(2, lSecond.getNextPage().getPageNumber());
        assertTrue(lSecond.getPreviousPage() == lFirst);

        final String lExpected1 = "< org.hip.kernel.bom.impl.PageImpl PageNumber=1 PageSize=3 />";
        final String lExpected2 = "< org.hip.kernel.bom.impl.PageImpl PageNumber=2 PageSize=3 />";
        assertEquals(lExpected1, lFirst.toString());
        assertEquals(lExpected2, lSecond.toString());

        int i = 0;
        //proceed through first page
        while (lFirst.hasMoreElements()) {
            lPage.nextElement();
            i++;
        }
        assertEquals(3, i);
        i = 0;
        //proceed through second page
        while (lSecond.hasMoreElements()) {
            lSecond.nextElement();
            i++;
        }
        assertEquals(1, i);

        //print out all pages
        while (!lPage.isLastPage()) {
            System.out.println(">>>>> Page " + lPage.getPageNumber() + ":");
            System.out.println(lPage.pageAsXML());
            lPage = lPage.getNextPage();
        }
        System.out.println(">>>>> last Page " + lPage.getPageNumber() + ":");
        System.out.println(lPage.pageAsXML());
    }

    @Test
    public void testSerialization() throws SQLException, NamingException, VException, IOException, ClassNotFoundException {
        final String[] lNames = {"1 eins", "2 zwei", "3 drei", "4 vier"};

        DataHouseKeeper.INSTANCE.createTestEntry(lNames[3]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[2]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[1]);
        DataHouseKeeper.INSTANCE.createTestEntry(lNames[0]);

        final OrderObject lOrder = new OrderObjectImpl();
        lOrder.setValue(Test2DomainObjectHomeImpl.KEY_NAME, 1);
        Page lPage = new PageImpl(DataHouseKeeper.INSTANCE.getSimpleHome().select(lOrder), null, 3);


        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lPage);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lPage = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final Page lRetrieved = (Page)lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        final Page lSecond = lRetrieved.getNextPage();
        final String lExpected1 = "< org.hip.kernel.bom.impl.PageImpl PageNumber=1 PageSize=3 />";
        final String lExpected2 = "< org.hip.kernel.bom.impl.PageImpl PageNumber=2 PageSize=3 />";
        assertEquals(lExpected1, lRetrieved.toString());
        assertEquals(lExpected2, lSecond.toString());
    }

}
