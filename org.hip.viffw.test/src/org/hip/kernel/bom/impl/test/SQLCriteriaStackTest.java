package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.ICriteriaStack;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.SQLCriteriaStack;
import org.junit.jupiter.api.Test;

public class SQLCriteriaStackTest {

    @Test
    void testRender() {
        ICriteriaStack lStack = new SQLCriteriaStack();
        assertEquals("", lStack.render());

        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("tbl.A", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("tbl.A AND tbl.B", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("tbl.A AND tbl.B AND tbl.C", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        assertEquals("(tbl.A AND tbl.B AND tbl.C) OR tbl.D", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("tbl.E"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        assertEquals("((tbl.A AND tbl.B AND tbl.C) OR tbl.D) OR tbl.E", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("tbl.E"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("tbl.F"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("(((tbl.A AND tbl.B AND tbl.C) OR tbl.D) OR tbl.E) AND tbl.F", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.D"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("((tbl.A OR tbl.B) AND tbl.C) AND tbl.D", lStack.render());

        lStack = new SQLCriteriaStack();
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.XOR);
        assertEquals("tbl.A XOR tbl.B", lStack.render());
    }

    @Test
    void testRender2() {
        ICriteriaStack lStack = new SQLCriteriaStack(", ");
        System.out.println(lStack.render());

        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("tbl.A", lStack.render());

        lStack = new SQLCriteriaStack(", ");
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("tbl.A, tbl.B", lStack.render());

        lStack = new SQLCriteriaStack(", ");
        lStack.addCriterium(new StringBuilder("tbl.A"));
        lStack.addCriterium(new StringBuilder("tbl.B"));
        lStack.addCriterium(new StringBuilder("tbl.C"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        assertEquals("tbl.A, tbl.B, tbl.C", lStack.render());
    }

}
