package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;

import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.DefaultQueryResult;
import org.hip.kernel.bom.impl.test.DataHouseKeeper;
import org.hip.kernel.servlet.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class PageableHtmlPageTest {

    private static final String NL = "" + (char)13 + (char)10;
    private static final String EXPECTED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
            "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
            "<title>VIF</title>\n</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + NL +
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>";
    //	private final static String EXPECTED1 = "<html><head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n<title>VIF</title>\n" +
    //		"</head><body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">\n" +
    //		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
    //		"<transformed><Root>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 1</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>476</TestID>\n" +
    //		"        <Mutation>2001-11-07 16:47:05.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 10</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>485</TestID>\n" +
    //		"        <Mutation>2001-11-07 16:47:06.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 11</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>486</TestID>\n" +
    //		"        <Mutation>2001-11-07 16:47:06.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 12</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>487</TestID>\n" +
    //		"        <Mutation>2001-11-07 16:47:06.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject></Root></transformed></body></html>";

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws Exception {
        final Context lContext = new TestContext();
        TestPrintWriter lWriter = new TestPrintWriter();

        final TestPageableHtmlPage lPage = new TestPageableHtmlPage(lContext);

        for (int i = 1; i < 16; i++) {
            DataHouseKeeper.INSTANCE.createTestEntry("test " + i);
        }

        final ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        final QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult,
                null);
        final TestXSLQueryResultView lResultView = new TestXSLQueryResultView(lContext, lQueryResult, 15);
        lPage.add(lResultView);

        //first page
        lPage.renderToWriter(lWriter, "");
        String lRendering = lWriter.getStreamedText();
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //last page
        lPage.setToLastPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 10</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 6</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //next page is last page again
        lPage.nextPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 10</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 6</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //previous page
        lPage.previousPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 3</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 4</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 5</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 6</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 7</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //first page
        lPage.setToFirstPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //first page again
        lPage.previousPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals(0, lRendering.indexOf(EXPECTED));
        assertTrue(lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lRendering.indexOf("<Name>test 2</Name>"));
    }
}
