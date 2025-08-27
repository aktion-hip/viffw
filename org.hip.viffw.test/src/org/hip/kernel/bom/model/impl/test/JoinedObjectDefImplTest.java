package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.hip.kernel.bom.SettingException;
import org.hip.kernel.bom.model.ColumnDefDef;
import org.hip.kernel.bom.model.HiddenDef;
import org.hip.kernel.bom.model.JoinedObjectDef;
import org.hip.kernel.bom.model.JoinedObjectDefDef;
import org.hip.kernel.bom.model.MappingDef;
import org.hip.kernel.bom.model.PropertyDef;
import org.hip.kernel.bom.model.impl.JoinedObjectDefGenerator;
import org.hip.kernel.bom.model.impl.JoinedObjectDefImpl;
import org.hip.kernel.util.DefaultNameValueList;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class JoinedObjectDefImplTest {
    // We create a simple object def
    private final Object[][] constObjectDef = {
            { JoinedObjectDefDef.objectName, "Person" }
            , { JoinedObjectDefDef.parent, "BusinessObject" }
            , { JoinedObjectDefDef.version, "1.0" }
            , { JoinedObjectDefDef.columnDefs, null }
            , { JoinedObjectDefDef.joinDef, null }
    };

    private final static String XML_OBJECT_DEF1 =
            "<?xml version='1.0' encoding='ISO-8859-1'?>	\n"
                    +
                    "<joinedObjectDef objectName='TestJoin1' parent='org.hip.kernel.bom.ReadOnlyDomainObject' version='1.0'>	\n"
                    +
                    "	<columnDefs>	\n"
                    +
                    "		<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='Name' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='FirstName' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='Mutation' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "	</columnDefs>	\n"
                    +
                    "	<joinDef joinType='EQUI_JOIN' operatorType='eq'>	\n"
                    +
                    "		<objectDesc objectClassName='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<objectDesc objectClassName='org.hip.kernel.bom.impl.test.LinkGroupMemberImpl'/>	\n"
                    +
                    "		<joinCondition>	\n"
                    +
                    "			<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "			<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.LinkGroupMemberImpl'/>	\n"
                    +
                    "		</joinCondition>	\n" +
                    "	</joinDef>	\n" +
                    "</joinedObjectDef>";
    private final static String XML_OBJECT_DEF2 =
            "<?xml version='1.0' encoding='ISO-8859-1'?>	\n"
                    +
                    "<joinedObjectDef objectName='TestJoin2' parent='org.hip.kernel.bom.ReadOnlyDomainObject' version='1.0'>	\n"
                    +
                    "	<columnDefs>	\n"
                    +
                    "		<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='Name' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='FirstName' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<columnDef columnName='Mutation' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<hidden columnName='GroupID' domainObject='org.hip.kernel.bom.impl.test.LinkGroupMemberImpl'/>	\n"
                    +
                    "	</columnDefs>	\n"
                    +
                    "	<joinDef joinType='EQUI_JOIN' operatorType='eq'>	\n"
                    +
                    "		<objectDesc objectClassName='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "		<objectDesc objectClassName='org.hip.kernel.bom.impl.test.LinkGroupMemberImpl'/>	\n"
                    +
                    "		<joinCondition>	\n"
                    +
                    "			<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>	\n"
                    +
                    "			<columnDef columnName='MemberID' domainObject='org.hip.kernel.bom.impl.test.LinkGroupMemberImpl'/>	\n"
                    +
                    "		</joinCondition>	\n" +
                    "	</joinDef>	\n" +
                    "</joinedObjectDef>";

    @Test
    public void testAddColumnDef() throws Exception {
        final JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        assertNotNull(lDef);

        // We create a column def and add it to the object def
        final DefaultNameValueList lList1 = new DefaultNameValueList();
        lList1.setValue(ColumnDefDef.columnName, "MemberID");
        lList1.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");

        assertNotNull(lList1);
        lDef.addColumnDef(lList1);

        // We create a column def and add it to the object def
        final DefaultNameValueList lList2 = new DefaultNameValueList();
        lList2.setValue(ColumnDefDef.columnName, "FirstName");
        lList2.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");

        assertNotNull(lList2);
        lDef.addColumnDef(lList2);

        // We create a column def and add it to the object def
        final DefaultNameValueList lList3 = new DefaultNameValueList();
        lList3.setValue(ColumnDefDef.columnName, "Mutation");
        lList3.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");

        assertNotNull(lList3);
        lDef.addColumnDef(lList3);

        assertEquals("tblTestMember.TESTMEMBERID", lDef.getColumnName(lList1));
        assertEquals("tblTestMember.SFIRSTNAME", lDef.getColumnName(lList2));
        assertEquals("tblTestMember.DTMUTATION", lDef.getColumnName(lList3));
    }

    // <columnDef columnName='FirstName' nestedObject='SelectNested'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    @Test
    public void testAddColumnDef2() throws Exception {
        final String lExpectedSQL = "SelectNested.SFIRSTNAME";
        final String lExpectedColumn = "FirstName";
        final JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        final DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn);
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.nestedObject, "SelectNested");

        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL,
                ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs)).iterator().next());

        final MappingDef lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn).getMappingDef();
        assertEquals("tblTestMember", lMapping.getTableName());
        assertEquals("SFIRSTNAME", lMapping.getColumnName());
    }

    // <columnDef columnName='FirstName' nestedObject='SelectNested' valueType='String'/>
    @Test
    public void testAddColumnDef3() throws Exception {
        final String lExpectedSQL = "SelectNested.FIRSTNAME";
        final String lExpectedColumn = "FIRSTNAME";
        final JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        final DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn);
        lList.setValue(ColumnDefDef.nestedObject, "SelectNested");
        lList.setValue(ColumnDefDef.valueType, "String");

        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL,
                ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs)).iterator().next());

        final MappingDef lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn).getMappingDef();
        assertEquals("SelectNested", lMapping.getTableName());
        assertEquals(lExpectedColumn, lMapping.getColumnName());
    }

    // <columnDef columnName='FirstName' as='AsSomethingDifferent' nestedObject='SelectNested'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    // <columnDef columnName='FirstName' as='AsSomethingDifferent'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    @Test
    public void testAddColumnDef4() throws Exception {
        final String lExpectedSQL1 = "SelectNested.SFIRSTNAME AS ASSOMETHINGDIFFERENT";
        final String lExpectedSQL2 = "tblTestMember.SFIRSTNAME AS ASSOMETHINGDIFFERENT";
        final String lExpectedColumn = "ASSOMETHINGDIFFERENT";
        JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, "FirstName");
        lList.setValue(ColumnDefDef.nestedObject, "SelectNested");
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.as, lExpectedColumn);

        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL1, ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs))
                .iterator().next());

        MappingDef lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn).getMappingDef();
        assertEquals("", lMapping.getTableName());
        assertEquals(lExpectedColumn, lMapping.getColumnName());

        lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, "FirstName");
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.as, lExpectedColumn);

        lDef = new JoinedObjectDefImpl(this.constObjectDef);
        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL2, ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs))
                .iterator().next());

        lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn).getMappingDef();
        assertEquals("", lMapping.getTableName());
        assertEquals(lExpectedColumn, lMapping.getColumnName());
    }

    // <columnDef columnName='FirstName' alias='AliasSomethingDifferent'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    @Test
    public void testAddColumnDef5() throws Exception {
        final String lExpectedSQL = "tblTestMember.SFIRSTNAME";
        final String lExpectedColumn = "AliasSomethingDifferent";
        final JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        final DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, "FirstName");
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.alias, lExpectedColumn);

        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL, ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs))
                .iterator().next());

        final MappingDef lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn).getMappingDef();
        assertEquals("tblTestMember", lMapping.getTableName());
        assertEquals("SFIRSTNAME", lMapping.getColumnName());
    }

    // <columnDef columnName='Mutation' template='NOW() > {0}'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    // <columnDef columnName='Mutation' template='NOW() > {0}' as='isActive'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    // <columnDef columnName='hasSuspended' template='testSuspended1 AND testSuspended2' as='hasSuspended'
    // nestedObject='SelectNested' valueType='Number'/>
    // <columnDef columnName='Mutation' template='NOW() > {0}' as='isActive' valueType='Number'
    // domainObject='org.hip.kernel.bom.impl.test.TestDomainObjectImpl'/>
    @Test
    public void testAddColumnDef6() throws Exception {
        final String lExpectedSQL1 = "(NOW() > tblTestMember.DTMUTATION)";
        final String lExpectedSQL2 = "(NOW() > tblTestMember.DTMUTATION) AS ISACTIVE";
        final String lExpectedSQL3 = "(testSuspended1 AND testSuspended2) AS HASSUSPENDED";

        final String lExpectedColumn1 = "Mutation";
        final String lExpectedColumn2 = "ISACTIVE";
        final String lExpectedColumn3 = "HASSUSPENDED";

        JoinedObjectDef lDef = new JoinedObjectDefImpl(this.constObjectDef);
        DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn1);
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.template, "NOW() > {0}");

        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL1, ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs))
                .iterator().next());

        MappingDef lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn1).getMappingDef();
        assertEquals("tblTestMember", lMapping.getTableName());
        assertEquals("DTMUTATION", lMapping.getColumnName());
        //
        lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn1);
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.template, "NOW() > {0}");
        lList.setValue(ColumnDefDef.as, lExpectedColumn2);

        lDef = new JoinedObjectDefImpl(this.constObjectDef);
        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL2,
                ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs)).iterator().next());

        lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn2).getMappingDef();
        assertEquals("", lMapping.getTableName());
        assertEquals(lExpectedColumn2, lMapping.getColumnName());
        //
        lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn3);
        lList.setValue(ColumnDefDef.template, "testSuspended1 AND testSuspended2");
        lList.setValue(ColumnDefDef.as, lExpectedColumn3);
        lList.setValue(ColumnDefDef.nestedObject, "SelectNested");
        lList.setValue(ColumnDefDef.valueType, "Number");

        lDef = new JoinedObjectDefImpl(this.constObjectDef);
        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL3,
                ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs)).iterator().next());

        lMapping = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn3).getMappingDef();
        assertEquals("", lMapping.getTableName());
        assertEquals(lExpectedColumn3, lMapping.getColumnName());
        //
        lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, lExpectedColumn1);
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");
        lList.setValue(ColumnDefDef.template, "NOW() > {0}");
        lList.setValue(ColumnDefDef.as, lExpectedColumn2);
        lList.setValue(ColumnDefDef.valueType, "Number");

        lDef = new JoinedObjectDefImpl(this.constObjectDef);
        lDef.addColumnDef(lList);

        assertEquals(lExpectedSQL2,
                ((Collection<?>) lDef.get(JoinedObjectDefDef.columnDefs)).iterator().next());

        final PropertyDef lPropertyDef = lDef.getDomainObjectDef().getPropertyDef(lExpectedColumn2);
        lMapping = lPropertyDef.getMappingDef();
        assertEquals("", lMapping.getTableName());
        assertEquals(lExpectedColumn2, lMapping.getColumnName());
        assertEquals("Number", lPropertyDef.getValueType());
    }

    @Test
    public void testCreation() throws Exception {
        final String[] lExpected = { "version", "columnDefs", "joinDef", "parent", "objectName" };
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));
        JoinedObjectDef lDef = new JoinedObjectDefImpl();
        assertNotNull(lDef);

        int i = 0;
        for (final String lName : lDef.getPropertyNames2()) {
            assertTrue(lVExpected.contains(lName));
        }

        lDef = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(XML_OBJECT_DEF1);
        i = 0;
        for (final String lName : lDef.getPropertyNames2()) {
            assertTrue(lVExpected.contains(lName));
        }

        assertEquals("TestJoin1", lDef.get(JoinedObjectDefDef.objectName));
        assertEquals("org.hip.kernel.bom.ReadOnlyDomainObject", lDef.get(JoinedObjectDefDef.parent));
        assertEquals("1.0", lDef.get(JoinedObjectDefDef.version));
    }

    @Test
    public void testEquals() throws Exception {
        final JoinedObjectDef lObjectDef1 = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF1);
        final JoinedObjectDef lObjectDef2 = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF2);
        final JoinedObjectDef lObjectDef3 = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF1);

        assertTrue(lObjectDef1.equals(lObjectDef3));
        assertEquals(lObjectDef1.hashCode(), lObjectDef3.hashCode());
        assertTrue(!lObjectDef1.equals(lObjectDef2));
        assertTrue(lObjectDef1.hashCode() != lObjectDef2.hashCode());
    }

    @Test
    public void testGetColumnName() throws Exception {
        final JoinedObjectDef lObjectDef = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF1);

        final DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(ColumnDefDef.columnName, "MemberID");
        lList.setValue(ColumnDefDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");

        assertEquals("tblTestMember.TESTMEMBERID", lObjectDef.getColumnName(lList));
    }

    @Test
    public void testGetTableName() throws Exception {
        final JoinedObjectDef lObjectDef = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF1);

        assertEquals("tblTestMember",
                lObjectDef.getTableName("org.hip.kernel.bom.impl.test.TestDomainObjectImpl"));
        assertEquals("tblGroupAdmin",
                lObjectDef.getTableName("org.hip.kernel.bom.impl.test.LinkGroupMemberImpl"));
    }

    @Test
    public void testSet() throws Exception {
        final JoinedObjectDef lDef = new JoinedObjectDefImpl();
        assertNotNull(lDef);

        // correct set
        try {
            lDef.set("version", "1.0");
        } catch (final SettingException exc) {
            fail("set 1");
        }

        // set with invalid value
        try {
            lDef.set("version", new Integer(1));
            fail("set 2");
        } catch (final SettingException exc) {
        }

        // set with invalid value
        try {
            lDef.set("version!", "1.1");
            fail("set 3");
        } catch (final SettingException exc) {
        }

        try {
            lDef.set(JoinedObjectDefDef.columnDefs, new Vector<String>());
        } catch (final SettingException exc) {
            fail("set 4");
        }

        try {
            lDef.set(JoinedObjectDefDef.joinDef, new org.hip.kernel.bom.model.impl.JoinDefImpl());
        } catch (final SettingException exc) {
            fail("set 5");
        }
    }

    @Test
    void testHidden() throws Exception {
        final JoinedObjectDef lObjectDef = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF2);
        assertEquals("tblGroupAdmin.GROUPID", lObjectDef.getHidden("GroupID"));
    }

    @Test
    void testAddHidden() throws Exception {
        final String lHidden = "MemberID";
        final DefaultNameValueList lList = new DefaultNameValueList();
        lList.setValue(HiddenDef.columnName, lHidden);
        lList.setValue(HiddenDef.domainObject, "org.hip.kernel.bom.impl.test.TestDomainObjectImpl");

        final JoinedObjectDef lObjectDef = new JoinedObjectDefImpl();
        lObjectDef.addHidden(lList);
        assertEquals("tblTestMember.TESTMEMBERID", lObjectDef.getHidden(lHidden));
    }

    @Test
    void testToString() throws Exception {
        final JoinedObjectDef lObjectDef = JoinedObjectDefGenerator.getSingleton().createJoinedObjectDef(
                XML_OBJECT_DEF1);
        assertEquals(
                "< org.hip.kernel.bom.model.impl.JoinedObjectDefImpl objectName=\"TestJoin1\" parent=\"org.hip.kernel.bom.ReadOnlyDomainObject\" version=\"1.0\" />",
                lObjectDef.toString());
    }
}
