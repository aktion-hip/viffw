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
public class XSLQueryResultViewTest {
    //	private static final String EXPECTED1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
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
    //		"        <TestID>341</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
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
    //		"        <TestID>350</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
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
    //		"        <TestID>351</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
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
    //		"        <TestID>352</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject></Root></transformed>";
    //	private static final String EXPECTED2 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
    //		"<transformed><Root>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 13</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>353</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 14</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>354</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 15</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>355</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject>\n" +
    //		"<TestDomainObject>\n" +
    //		"    <propertySet>\n" +
    //		"        <PLZ/>\n" +
    //		"        <Language/>\n" +
    //		"        <Fax/>\n" +
    //		"        <Tel/>\n" +
    //		"        <Firstname>Adam</Firstname>\n" +
    //		"        <Name>test 2</Name>\n" +
    //		"        <Street/>\n" +
    //		"        <Amount>12.45</Amount>\n" +
    //		"        <Password/>\n" +
    //		"        <Sex>1</Sex>\n" +
    //		"        <City/>\n" +
    //		"        <Mail>dummy1@aktion-hip.ch</Mail>\n" +
    //		"        <Double>13.11</Double>\n" +
    //		"        <TestID>342</TestID>\n" +
    //		"        <Mutation>2001-11-02 12:35:40.0</Mutation>\n" +
    //		"    </propertySet>\n" +
    //		"</TestDomainObject></Root></transformed>";

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws Exception {
        final Context lContext = new TestContext();
        //		TestOutputStream lStream = new TestOutputStream();
        //		lStream.setFlushMode(false);
        TestPrintWriter lWriter = new TestPrintWriter();

        for (int i = 1; i < 16; i++) {
            DataHouseKeeper.INSTANCE.createTestEntry("test " + i);
        }

        final ResultSet lResult = DataHouseKeeper.INSTANCE
                .executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        final QueryResult lQueryResult = new DefaultQueryResult(DataHouseKeeper.INSTANCE.getSimpleHome(), lResult,
                null);
        final TestXSLQueryResultView lResultView = new TestXSLQueryResultView(lContext, lQueryResult, 15);
        assertEquals(4, lResultView.getPageSize());

        lResultView.renderToWriter(lWriter, "");
        String lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        lResultView.nextPage();
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 13</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 14</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 15</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 2</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 1</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 3</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        lResultView.previousPage();
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //jumps to last page
        lResultView.nextPage(3);
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 10</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 6</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //jumps back to second page
        lResultView.previousPage(2);
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 13</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 14</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 15</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 2</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 1</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 3</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //jumps to last page again
        lResultView.nextPage(5);
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 11</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //sets to first page
        lResultView.setToFirstPage();
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 13</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();

        //sets to first page
        lResultView.setToLastPage();
        lResultView.renderToWriter(lWriter, "");
        lStreamed = lWriter.getStreamedText();
        assertTrue(lStreamed.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue(lStreamed.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals(-1, lStreamed.indexOf("<Name>test 11</Name>"));
        assertEquals(-1, lStreamed.indexOf("<Name>test 2</Name>"));
    }
}
