package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.bom.model.JoinOperation;
import org.hip.kernel.bom.model.PlaceholderDef;
import org.hip.kernel.bom.model.impl.JoinOperationImpl;
import org.hip.kernel.bom.model.impl.PlaceholderDefImpl;
import org.junit.jupiter.api.Test;

/**
 * JoinOperationImplTest.java
 *
 * Created on 08.09.2002
 * @author Benno Luthiger
 */
public class JoinOperationImplTest {

    @Test
    public void testEquals() {
        final JoinOperation lJoinOperation1 = new JoinOperationImpl();
        lJoinOperation1.addJoinCondition(null);
        lJoinOperation1.setTableName("tblTestMember");
        lJoinOperation1.setTableName("tblLinkMember");
        lJoinOperation1.setJoinOperand("tblTestMember.TESTMEMBERID");
        lJoinOperation1.setJoinOperand("tblLinkMember.MEMBERID");
        lJoinOperation1.setJoinType("EQUI_JOIN");

        final JoinOperation lJoinOperation2 = new JoinOperationImpl();
        lJoinOperation2.addJoinCondition(null);
        lJoinOperation2.setTableName("tblTestMember");
        lJoinOperation2.setTableName("tblLinkMember");
        lJoinOperation2.setJoinOperand("tblTestMember.TESTMEMBERID");
        lJoinOperation2.setJoinOperand("tblLinkMember.MEMBERID");
        lJoinOperation2.setJoinType("EQUI_JOIN");

        assertTrue(lJoinOperation1.equals(lJoinOperation2));

        lJoinOperation2.setJoinType("LEFT_JOIN_OUTER");
        assertTrue(!lJoinOperation1.equals(lJoinOperation2));

        final JoinOperation lJoinOperation3 = new JoinOperationImpl();
        lJoinOperation3.addJoinCondition(null);
        lJoinOperation3.setTableName("tblTestMember");
        lJoinOperation3.setJoinOperand("tblTestMember.TESTMEMBERID");
        lJoinOperation3.setJoinOperand("tblLinkMember.MEMBERID");
        lJoinOperation3.setJoinType("EQUI_JOIN");
        assertTrue(!lJoinOperation1.equals(lJoinOperation3));
    }

    @Test
    public void testToString() {
        final String lExpected = "< org.hip.kernel.bom.model.impl.JoinOperationImpl left_tablename=\"tblTestMember\", right_tablename=\"tblLinkMember\", join_type=\"EQUI_JOIN\", SQL [tblTestMember.TESTMEMBERID = tblLinkMember.MEMBERID] />";
        final JoinOperation lJoinOperation1 = new JoinOperationImpl();
        lJoinOperation1.addJoinCondition(null);
        lJoinOperation1.setTableName("tblTestMember");
        lJoinOperation1.setTableName("tblLinkMember");
        lJoinOperation1.setJoinOperand("tblTestMember.TESTMEMBERID");
        lJoinOperation1.setJoinOperand("tblLinkMember.MEMBERID");
        lJoinOperation1.setJoinType("EQUI_JOIN");

        assertEquals(lExpected, lJoinOperation1.toString());
    }

    @Test
    public void testAddPlaceholderDef() {
        final String lExpected = "Test-Placeholder";
        final PlaceholderDef lPlaceholder = new PlaceholderDefImpl(lExpected);

        final JoinOperation lJoinOperation = new JoinOperationImpl();
        lJoinOperation.setTableName("Left");

        assertFalse(lJoinOperation.hasPlaceholder(lExpected));

        lJoinOperation.addPlaceholderDef(lPlaceholder);
        assertTrue(lJoinOperation.hasPlaceholder(lExpected));
    }

    @Test
    public void testRenderSQL() throws Exception {
        final JoinOperation lJoinOperation = new JoinOperationImpl();
        lJoinOperation.setTableName("tblTestMember");
        lJoinOperation.setTableName("tblLinkMember");
        lJoinOperation.setJoinType("EQUI_JOIN");

        lJoinOperation.addJoinCondition(null);
        lJoinOperation.setJoinOperand("tblTestMember.TESTMEMBERID");
        lJoinOperation.setJoinOperand("tblLinkMember.MEMBERID");

        lJoinOperation.addJoinCondition("AND");
        lJoinOperation.setJoinOperand("tblTestMember.VERSION");
        lJoinOperation.setJoinOperand("tblLinkMember.VERSION");

        final String lExpected = "tblTestMember.TESTMEMBERID = tblLinkMember.MEMBERID AND tblTestMember.VERSION = tblLinkMember.VERSION";
        assertEquals(lExpected, lJoinOperation.renderSQL());
    }
}
