package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.SettingException;
import org.hip.kernel.bom.model.JoinDef;
import org.hip.kernel.bom.model.JoinDefDef;
import org.hip.kernel.bom.model.impl.JoinDefImpl;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class JoinDefImplTest {

    @Test
    public void testEquals() throws Exception {

        final JoinDef lDef1 = new JoinDefImpl();
        final JoinDef lDef2 = new JoinDefImpl();
        final JoinDef lDef3 = new JoinDefImpl();

        assertNotNull(lDef1);
        assertNotNull(lDef2);
        assertNotNull(lDef3);

        lDef1.set(JoinDefDef.joinType, "EQUI_JOIN");
        lDef1.setTableName("tblLeft");
        lDef1.setTableName("tblRight");
        lDef1.addJoinCondition(null);
        lDef1.addColumnDef("column1");
        lDef1.addColumnDef("column2");

        lDef2.set(JoinDefDef.joinType, "LEFT_JOIN");
        lDef2.setTableName("tblLeft");
        lDef2.setTableName("tblRight");
        lDef2.addJoinCondition(null);
        lDef2.addColumnDef("column1");
        lDef2.addColumnDef("column2");

        lDef3.set(JoinDefDef.joinType, "EQUI_JOIN");
        lDef3.setTableName("tblLeft");
        lDef3.setTableName("tblRight");
        lDef3.addJoinCondition(null);
        lDef3.addColumnDef("column1");
        lDef3.addColumnDef("column2");

        assertTrue(lDef1.equals(lDef3));
        assertEquals(lDef1.hashCode(), lDef3.hashCode());

        assertTrue(!lDef1.equals(lDef2));
        assertTrue(lDef1.hashCode() != lDef2.hashCode());
    }

    @Test
    public void testSet() throws Exception {

        final JoinDef lDef = new JoinDefImpl() ;
        assertNotNull(lDef);

        lDef.set(JoinDefDef.joinType, "EQUI_JOIN");
        lDef.setTableName("tblLeft");
        lDef.setTableName("tblRight");
        lDef.addJoinCondition(null);
        lDef.addColumnDef("column1");
        lDef.addColumnDef("column2");

        //incorrect type
        try {
            lDef.set(JoinDefDef.joinType, new Integer(0));
            fail("incorrect type");
        }
        catch (final SettingException exc) {
        }
    }

    @Test
    public void testToString() throws Exception {

        final JoinDef lDef = new JoinDefImpl();
        assertNotNull(lDef);

        final JoinDef lOuterDef = new JoinDefImpl();
        assertNotNull(lOuterDef);

        lDef.set(JoinDefDef.joinType, "EQUI_JOIN");
        lDef.setTableName("tblLeft");
        lDef.setTableName("tblRight");
        lDef.addJoinCondition(null);
        lDef.addColumnDef("column1");
        lDef.addColumnDef("column2");
        final String lExp1 = "< org.hip.kernel.bom.model.impl.JoinDefImpl joinType=\"EQUI_JOIN\" />";

        assertEquals(lExp1, lDef.toString());

        lOuterDef.set(JoinDefDef.joinType, "LEFT_JOIN");
        lOuterDef.setTableName("tblLeftOuter");
        lOuterDef.setChildJoinDef(lDef);
        lOuterDef.addJoinCondition(null);
        lOuterDef.addColumnDef("outerColumn1");
        lOuterDef.addColumnDef("outerColumn2");
        final String lExp2 = "< org.hip.kernel.bom.model.impl.JoinDefImpl joinType=\"LEFT_JOIN\" />";

        assertEquals(lExp2, lOuterDef.toString());
    }
}
