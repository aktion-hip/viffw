package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.PropertySet;
import org.hip.kernel.bom.impl.CsvSerializer;
import org.hip.kernel.bom.impl.DftSerializer;
import org.hip.kernel.bom.impl.PropertySetImpl;
import org.hip.kernel.bom.impl.XMLSerializer;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class SerializerTest {

    @Test
    public void testCsvSerializer() {
        CsvSerializer lSerializer = new CsvSerializer();

        try {
            final Test2DomainObjectHomeImpl lHome = (Test2DomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");

            final String lName = "Dummy";
            final String lFirstname = "Fi";
            final String lMail = "dummy1@aktion-hip.ch";

            //create domain object
            final DomainObject lNew = lHome.create();
            lNew.set("Name", lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Mail", lMail);
            lNew.set("Sex", new Integer(1));
            lNew.set("Amount", new Float(12.45));
            lNew.set("Double", new Float(13.11));

            //test serialization of domain object
            //			String lExpected1 = ",,,,Fi,Dummy,,12.45,,1,,dummy1@aktion-hip.ch,13.11,,,";
            lNew.accept(lSerializer);
            String lSerialized = lSerializer.toString();
            assertTrue(lSerialized.length() > 50);
            assertTrue(lSerialized.length() < 55);
            assertTrue(lSerialized.indexOf("Fi,") >= 0);
            assertTrue(lSerialized.indexOf("Dummy,") >= 0);
            assertTrue(lSerialized.indexOf("12.45,") >= 0);
            assertTrue(lSerialized.indexOf("1,") >= 0);
            assertTrue(lSerialized.indexOf("dummy1@aktion-hip.ch,") >= 0);
            assertTrue(lSerialized.indexOf("13.11,") >= 0);

            //create property set
            final PropertySet lSet = new PropertySetImpl(null);
            lSet.setValue("test String", lName);
            lSet.setValue("test Numeric", new Float(11.55));

            //test serialization of property set
            lSerializer = new CsvSerializer();
            lSet.accept(lSerializer);
            lSerialized = lSerializer.toString();
            assertEquals(12, lSerialized.length());
            assertTrue(lSerialized.indexOf("11.55,") >= 0);
            assertTrue(lSerialized.indexOf("Dummy,") >= 0);

            final DomainObject lNew2 = lHome.create();
            lNew2.set("Name", "Beta");
            lNew2.set("Firstname", lFirstname);
            lNew2.set("Mail", lMail);
            lNew2.set("Sex", new Integer(1));
            lNew2.set("Amount", new Float(12.45));
            lNew2.set("Double", new Float(13.11));
            final DomainObject lNew3 = lHome.create();
            lNew3.set("Name", "Alpha");
            lNew3.set("Firstname", lFirstname);
            lNew3.set("Mail", lMail);
            lNew3.set("Sex", new Integer(1));
            lNew3.set("Amount", new Float(12.45));
            lNew3.set("Double", new Float(13.11));

            //create sorted array of domain objects
            final TestSortedArray lSortedArray = new TestSortedArray();
            lSortedArray.add(lNew);
            lSortedArray.add(lNew2);
            lSortedArray.add(lNew3);

            //test serialization of sorted array
            lSerializer = new CsvSerializer();
            lSortedArray.accept(lSerializer);
            lSerialized = lSerializer.toString();
            assertTrue(lSerialized.length() == 158);
            assertTrue(lSerialized.indexOf("Alpha") < lSerialized.indexOf("Beta"));
            assertTrue(lSerialized.indexOf("Beta") < lSerialized.indexOf(lName));
        }
        catch (final org.hip.kernel.exc.VException exc) {
            fail("testCreate f1 " + exc.getMessage());
        }
    }

    @Test
    public void testDftSerializer() {
        DftSerializer lSerializer = new DftSerializer();

        try {
            final Test2DomainObjectHomeImpl lHome = (Test2DomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");

            final String lName = "Dummy";
            final String lFirstname = "Fi";
            final String lMail = "dummy1@aktion-hip.ch";

            //create domain object
            final DomainObject lNew = lHome.create();
            lNew.set("Name", lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Mail", lMail);
            lNew.set("Sex", new Integer(1));
            lNew.set("Amount", new Float(12.45));
            lNew.set("Double", new Float(13.11));

            //test serialization of domain object
            //		String lExpected1 = "<TestDomainObject>\n" +
            //			"    <propertySet>\n" +
            //			"        <PLZ>\n" +
            //			"        <Language>\n" +
            //			"        <Fax>\n" +
            //			"        <Tel>\n" +
            //			"        <Firstname>Fi\n" +
            //			"        <Name>Dummy\n" +
            //			"        <Street>\n" +
            //			"        <Amount>12.45\n" +
            //			"        <Password>\n" +
            //			"        <Sex>1\n" +
            //			"        <City>\n" +
            //			"        <Mail>dummy1@aktion-hip.ch\n" +
            //			"        <Double>13.11\n" +
            //			"        <TestID>\n" +
            //			"        <Mutation>";
            lNew.accept(lSerializer);
            String lSerialized = lSerializer.toString();
            assertTrue(lSerialized.length() == 346);
            assertTrue(lSerialized.indexOf("<Firstname>Fi") > 0);
            assertTrue(lSerialized.indexOf("<Name>Dummy") > 0);
            assertTrue(lSerialized.indexOf("<Amount>12.45") > 0);
            assertTrue(lSerialized.indexOf("<Sex>1") > 0);
            assertTrue(lSerialized.indexOf("<Mail>dummy1@aktion-hip.ch") > 0);
            assertTrue(lSerialized.indexOf("<Double>13.11") > 0);
            assertTrue(lSerialized.indexOf("<TestDomainObject>") > 0);
            assertTrue(lSerialized.indexOf("<propertySet>") > 0);

            //create property set
            final PropertySet lSet = new PropertySetImpl(null);
            lSet.setValue("test String", lName);
            lSet.setValue("test Numeric", new Float(11.55));

            //test serialization of property set
            lSerializer = new DftSerializer();
            lSet.accept(lSerializer);
            lSerialized = lSerializer.toString();
            assertEquals(66, lSerialized.length());
            assertTrue(lSerialized.indexOf("<test Numeric>11.55") > 0);
            assertTrue(lSerialized.indexOf("<test String>Dummy") > 0);
            assertTrue(lSerialized.indexOf("<propertySet>") > 0);

            final DomainObject lNew2 = lHome.create();
            lNew2.set("Name", "Beta");
            lNew2.set("Firstname", lFirstname);
            lNew2.set("Mail", lMail);
            lNew2.set("Sex", new Integer(1));
            lNew2.set("Amount", new Float(12.45));
            lNew2.set("Double", new Float(13.11));
            final DomainObject lNew3 = lHome.create();
            lNew3.set("Name", "Alpha");
            lNew3.set("Firstname", lFirstname);
            lNew3.set("Mail", lMail);
            lNew3.set("Sex", new Integer(1));
            lNew3.set("Amount", new Float(12.45));
            lNew3.set("Double", new Float(13.11));

            //create sorted array of domain objects
            final TestSortedArray lSortedArray = new TestSortedArray();
            lSortedArray.add(lNew);
            lSortedArray.add(lNew2);
            lSortedArray.add(lNew3);

            //test serialization of sorted array
            lSerializer = new DftSerializer();
            lSortedArray.accept(lSerializer);
            lSerialized = lSerializer.toString();
            assertTrue(lSerialized.length() == 1282);
            assertTrue(lSerialized.indexOf("<Name>Alpha") < lSerialized.indexOf("<Name>Beta"));
            assertTrue(lSerialized.indexOf("<Name>Beta") < lSerialized.indexOf("<Name>" + lName));
        }
        catch (final org.hip.kernel.exc.VException exc) {
            fail("testCreate f1 " + exc.getMessage());
        }
    }

    @Test
    public void testXMLSerializer() {
        XMLSerializer lXMLSerializer = new XMLSerializer();

        try {
            final Test2DomainObjectHomeImpl lHome = (Test2DomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");

            final String lName = "Dummy";
            final String lFirstname = "Fi";
            final String lMail = "dummy1@aktion-hip.ch";

            //create domain object
            final DomainObject lNew = lHome.create();
            lNew.set("Name", lName);
            lNew.set("Firstname", lFirstname);
            lNew.set("Mail", lMail);
            lNew.set("Sex", new Integer(1));
            lNew.set("Amount", new Float(12.45));
            lNew.set("Double", new Float(13.11));

            //test serialization of domain object
            lNew.accept(lXMLSerializer);
            String lXML = lXMLSerializer.toString();
            assertTrue(lXML.length() > 500);
            assertTrue(lXML.length() < 510);
            assertTrue(lXML.indexOf("TestDomainObject") > 0);
            assertTrue(lXML.indexOf("propertySet") > 0);
            assertTrue(lXML.indexOf("<Firstname>Fi</Firstname>") > 0);
            assertTrue(lXML.indexOf("<Amount>12.45</Amount>") > 0);
            assertTrue(lXML.indexOf("<Sex>1</Sex>") > 0);
            assertTrue(lXML.indexOf("<Mail>dummy1@aktion-hip.ch</Mail>") > 0);
            assertTrue(lXML.indexOf("<Double>13.11</Double>") > 0);

            //create property set
            final PropertySet lSet = new PropertySetImpl(null);
            lSet.setValue("test String", lName);
            lSet.setValue("test Numeric", new Float(11.55));

            //test serialization of property set
            lXMLSerializer = new XMLSerializer();
            lSet.accept(lXMLSerializer);
            lXML = lXMLSerializer.toString();
            assertTrue(lXML.length() > 105);
            assertTrue(lXML.length() < 115);
            assertTrue(lXML.indexOf("propertySet") > 0);
            assertTrue(lXML.indexOf("<test Numeric>11.55</test Numeric>") > 0);
            assertTrue(lXML.indexOf("<test String>Dummy</test String>") > 0);

            final DomainObject lNew2 = lHome.create();
            lNew2.set("Name", "Beta");
            lNew2.set("Firstname", lFirstname);
            lNew2.set("Mail", lMail);
            lNew2.set("Sex", new Integer(1));
            lNew2.set("Amount", new Float(12.45));
            lNew2.set("Double", new Float(13.11));
            final DomainObject lNew3 = lHome.create();
            lNew3.set("Name", "Alpha");
            lNew3.set("Firstname", lFirstname);
            lNew3.set("Mail", lMail);
            lNew3.set("Sex", new Integer(1));
            lNew3.set("Amount", new Float(12.45));
            lNew3.set("Double", new Float(13.11));

            //create sorted array of domain objects
            final TestSortedArray lSortedArray = new TestSortedArray();
            lSortedArray.add(lNew);
            lSortedArray.add(lNew2);
            lSortedArray.add(lNew3);

            //test serialization of sorted array
            lXMLSerializer = new XMLSerializer();
            lSortedArray.accept(lXMLSerializer);
            lXML = lXMLSerializer.toString();
            assertTrue(lXML.length() > 1765);
            assertTrue(lXML.length() < 1775);
            assertTrue(lXML.indexOf("propertySet") > 0);
            assertTrue(lXML.indexOf("SortedArray") > 0);
            assertTrue(lXML.indexOf("TestDomainObject") > 0);
            assertTrue(lXML.indexOf("<Name>Alpha</Name>") < lXML.indexOf("<Name>Beta</Name>"));
            assertTrue(lXML.indexOf("<Name>Beta</Name>") < lXML.indexOf("<Name>" + lName + "</Name>"));
        }
        catch (final org.hip.kernel.exc.VException exc) {
            fail("testCreate f1 " + exc.getMessage());
        }
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException, VException {
        XMLSerializer lXMLSerializer = new XMLSerializer();

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lXMLSerializer);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lXMLSerializer = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final XMLSerializer lRetrieved = (XMLSerializer)lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        final Test2DomainObjectHomeImpl lHome = (Test2DomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");
        final DomainObject lNew = lHome.create();
        lNew.accept(lRetrieved);
        lRetrieved.toString();
    }

}
