package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.ICriteriaStack;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.LDAPCriteriaStack;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 * Created: 10.07.2007
 */
public class LDAPCriteriaStackTest {

    @Test
    void testRender() {
        ICriteriaStack lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("cn=A", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("&(cn=A)(cn=B)", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("&(cn=A)(cn=B)(cn=C)", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        assertEquals("|(&(cn=A)(cn=B)(cn=C))(cn=D)", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("cn=E"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        assertEquals("|(|(&(cn=A)(cn=B)(cn=C))(cn=D))(cn=E)", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=D"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("cn=E"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("cn=F"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("&(|(|(&(cn=A)(cn=B)(cn=C))(cn=D))(cn=E))(cn=F)", lStack.render());

        lStack = new LDAPCriteriaStack();
        lStack.addCriterium(new StringBuilder("cn=A"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=B"));
        lStack.addOperator(BinaryBooleanOperator.OR);
        lStack.addCriterium(new StringBuilder("cn=C"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        lStack.addCriterium(new StringBuilder("cn=D"));
        lStack.addOperator(BinaryBooleanOperator.AND);
        assertEquals("&(&(|(cn=A)(cn=B))(cn=C))(cn=D)", lStack.render());
    }

}
