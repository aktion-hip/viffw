package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.model.impl.JoinDefOperand;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 */
public class JoinDefOperandTest {

    @Test
    void testRender() throws Exception {
        JoinDefOperand lOperand = new JoinDefOperand();
        lOperand.setJoinOperand("TableLeft.Field1");
        lOperand.setJoinOperand("TableRight.Field1");
        assertEquals("TableLeft.Field1 = TableRight.Field1", lOperand.renderSQL());

        lOperand = new JoinDefOperand("AND");
        lOperand.setJoinOperand("TableLeft.Field1");
        lOperand.setJoinOperand("TableRight.Field1");
        assertEquals(" AND TableLeft.Field1 = TableRight.Field1", lOperand.renderSQL());
    }

}
