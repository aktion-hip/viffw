package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Collections;
import java.util.Vector;

import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.IGetValueStrategy;
import org.hip.kernel.bom.KeyCriterion;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.AbstractCriteriumRenderer;
import org.hip.kernel.bom.impl.BetweenObjectImpl;
import org.hip.kernel.bom.impl.CriteriaStackFactory;
import org.hip.kernel.bom.impl.InObjectImpl;
import org.hip.kernel.bom.impl.KeyCriterionImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.PreparedValueStrategy;
import org.hip.kernel.bom.impl.SQLRange;
import org.hip.kernel.exc.VException;
import org.hip.kernel.util.SortableItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class KeyObjectImplTest {


    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    public void testCreate() throws VException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();

        final KeyObject lKey1 = new KeyObjectImpl();
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(12));
        assertNotNull(lKey1);
        assertEquals("tblTest.FAMOUNT = 12", new String(lKey1.render2(lHome)));

        final KeyObject lKey2 = new KeyObjectImpl();
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_DOUBLE, new Integer(100));
        lKey2.setValue(lKey1);
        assertNotNull(lKey2);
        assertEquals(2, lKey2.size());
        assertEquals(
                "tblTest.FDOUBLE = 100 AND (tblTest.FAMOUNT = 12)",
                new String(lKey2.render2(lHome)));

        assertTrue(lKey2.isPrimaryKey());
        lKey2.setSchemaName("dummy");
        assertTrue(!lKey2.isPrimaryKey());

        final KeyObject lKey3 = new KeyObjectImpl();
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(101));
        lKey3.setValue(lKey1);
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(100));
        assertEquals(
                "tblTest.FAMOUNT = 101 AND (tblTest.FAMOUNT = 12) AND tblTest.FAMOUNT = 100",
                new String(lKey3.render2(lHome)));

        assertTrue(lKey2.equals(lKey2));
        assertEquals(lKey2.hashCode(), lKey2.hashCode());
        assertTrue(!lKey2.equals(lKey1));
        assertTrue(lKey2.hashCode() != lKey1.hashCode());

        // empty KeyObjects
        final KeyObject lKey4 = new KeyObjectImpl();
        assertTrue(lKey4.getItems2().isEmpty());
        assertEquals("", new String(lKey4.render2(lHome)));

        KeyObject lKey = new KeyObjectImpl();
        final Date lDate1 = Date.valueOf("1989-08-20");
        final Date lDate2 = Date.valueOf("1999-04-24");
        final SQLRange lBetween = new BetweenObjectImpl(lDate1, lDate2);
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_MUTATION, lBetween);
        assertEquals(
                "tblTest.DTMUTATION BETWEEN DATE('1989-08-20') AND DATE('1999-04-24')",
                new String(lKey.render2(lHome)));

        lKey = new KeyObjectImpl();
        final SQLRange lIn = new InObjectImpl<Integer>(new Integer[] { new Integer(65), new Integer(77),
                new Integer(87), new Integer(99) });
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, lIn);
        assertEquals(
                "tblTest.FAMOUNT IN (65, 77, 87, 99)",
                new String(lKey.render2(lHome)));

        // if we pass a collection, the key object creates a InObjectImpl() internally
        final Vector<Integer> lValues = new Vector<Integer>();
        Collections.addAll(lValues, new Integer(65), new Integer(77), new Integer(87), new Integer(99));
        lKey = new KeyObjectImpl();
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, lValues);
        assertEquals("tblTest.FAMOUNT IN (65, 77, 87, 99)", new String(lKey.render2(lHome)));
    }

    @Test
    public void testRender() throws VException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();

        KeyObject lKey1 = new KeyObjectImpl();
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "Ha%", "LIKE");
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Ha%", "LIKE", BinaryBooleanOperator.OR);
        assertEquals(
                "tblTest.SNAME LIKE 'Ha%' OR tblTest.SFIRSTNAME LIKE 'Ha%'",
                new String(lKey1.render2(lHome)));

        KeyObject lKey2 = new KeyObjectImpl();
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "A%", "LIKE");
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "B%", "LIKE");

        lKey1.setValue(lKey2, BinaryBooleanOperator.OR);
        assertEquals(
                "key 2",
                "tblTest.SNAME LIKE 'Ha%' OR tblTest.SFIRSTNAME LIKE 'Ha%' OR (tblTest.SNAME LIKE 'A%' AND tblTest.SFIRSTNAME LIKE 'B%')",
                new String(lKey1.render2(lHome)));

        final KeyObject lKey3 = new KeyObjectImpl();
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "Ha%", "LIKE");
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Ha%", "LIKE", BinaryBooleanOperator.OR);
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(87));
        assertEquals(
                "(tblTest.SNAME LIKE 'Ha%' OR tblTest.SFIRSTNAME LIKE 'Ha%') AND tblTest.FAMOUNT = 87",
                new String(lKey3.render2(lHome)));

        // test of ldap style key rendering
        lKey1 = new KeyObjectImpl();
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "Ha*");
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Ha*", "=", BinaryBooleanOperator.OR);

        lKey2 = new KeyObjectImpl();
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_NAME, "A*");
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "B*");

        lKey1.setValue(lKey2, BinaryBooleanOperator.OR);

        lKey1.setCriteriumRenderer(new LDAPCriteriumRenderer());
        lKey1.setCriteriaStackFactory(new CriteriaStackFactory(CriteriaStackFactory.StackType.LDAP));
        lKey1.setLevelReturnFormatter(KeyCriterionImpl.LEVEL_STRAIGHT);
        assertEquals(
                "|(tblTest.SNAME='Ha*')(tblTest.SFIRSTNAME='Ha*')(&(tblTest.SNAME='A*')(tblTest.SFIRSTNAME='B*'))",
                new String(lKey1.render2(lHome)));
    }

    @Test
    public void testRenderPrepared() throws VException {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();
        final IGetValueStrategy lGetValueStrategy = new PreparedValueStrategy();

        KeyObject lKey1 = new KeyObjectImpl();
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(12));
        lKey1.setGetValueStrategy(lGetValueStrategy);
        assertEquals("tblTest.FAMOUNT = ?", new String(lKey1.render2(lHome)));

        final KeyObject lKey2 = new KeyObjectImpl();
        lKey2.setValue(Test2DomainObjectHomeImpl.KEY_DOUBLE, new Integer(100));
        lKey2.setValue(lKey1);
        lKey2.setGetValueStrategy(lGetValueStrategy);
        assertEquals(
                "tblTest.FDOUBLE = ? AND (tblTest.FAMOUNT = ?)",
                new String(lKey2.render2(lHome)));

        KeyObject lKey3 = new KeyObjectImpl();
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(101));
        lKey3.setValue(lKey1);
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(100));
        lKey3.setGetValueStrategy(lGetValueStrategy);
        assertEquals(
                "tblTest.FAMOUNT = ? AND (tblTest.FAMOUNT = ?) AND tblTest.FAMOUNT = ?",
                new String(lKey3.render2(lHome)));

        lKey1 = new KeyObjectImpl();
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(12));
        lKey1.setValue(Test2DomainObjectHomeImpl.KEY_DOUBLE, new Integer(20));
        lKey3 = new KeyObjectImpl();
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_CITY, "City");
        lKey3.setValue(Test2DomainObjectHomeImpl.KEY_PLZ, "PLZ");
        lKey3.setValue(lKey1);
        lKey3.setGetValueStrategy(lGetValueStrategy);
        lKey3.setCriteriaStackFactory(new CriteriaStackFactory(CriteriaStackFactory.StackType.FLAT_JOIN, ", "));
        assertEquals(
                "tblTest.SCITY = ?, tblTest.SPLZ = ?, (tblTest.FAMOUNT = ?, tblTest.FDOUBLE = ?)",
                new String(lKey3.render2(lHome)));

        KeyObject lKey = new KeyObjectImpl();
        final Date lDate1 = Date.valueOf("1989-08-20");
        final Date lDate2 = Date.valueOf("1999-04-24");
        final SQLRange lBetween = new BetweenObjectImpl(lDate1, lDate2);
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_MUTATION, lBetween);
        lKey.setGetValueStrategy(lGetValueStrategy);
        assertEquals(
                "tblTest.DTMUTATION BETWEEN ? AND ?",
                new String(lKey.render2(lHome)));

        lKey = new KeyObjectImpl();
        final SQLRange lIn = new InObjectImpl<Integer>(new Integer[] { new Integer(65), new Integer(77),
                new Integer(87), new Integer(99) });
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_AMOUNT, lIn);
        lKey.setGetValueStrategy(lGetValueStrategy);
        assertEquals(
                "tblTest.FAMOUNT IN (?, ?, ?, ?)",
                new String(lKey.render2(lHome)));
    }

    @Test
    public void testSort() throws VException {
        final String[] lExpected = { "field1", "__keyObject(VIF)", "field2", "field3" };
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(lExpected[0], new Integer(12), "=", BinaryBooleanOperator.AND);
        final KeyObject lKeyInner = new KeyObjectImpl();
        lKeyInner.setValue(lExpected[2], "Test", "=");
        lKeyInner.setValue(lExpected[3], "test", "=", BinaryBooleanOperator.OR);
        lKey.setValue(lKeyInner, BinaryBooleanOperator.AND);

        int i = 0;
        for (final SortableItem lItem : lKey.getItems2()) {
            assertEquals(lExpected[i], ((KeyCriterion) lItem).getName());
            i++;
        }
    }

    // -- private classes --

    private class LDAPCriteriumRenderer extends AbstractCriteriumRenderer {
        @Override
        public StringBuffer render() {
            return new StringBuffer(render2());
        }

        @Override
        public StringBuilder render2() {
            final StringBuilder outSQL = new StringBuilder(this.operand1);
            outSQL.append(this.comparison).append(this.operand2);
            return outSQL;
        }
    }

}
