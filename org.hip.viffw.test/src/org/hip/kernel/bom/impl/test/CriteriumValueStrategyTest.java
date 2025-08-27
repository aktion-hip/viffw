package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.IGetValueStrategy;
import org.hip.kernel.bom.KeyCriterion;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.CriteriumValueStrategy;
import org.hip.kernel.bom.impl.KeyCriterionImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 * Created: 11.07.2007
 */
public class CriteriumValueStrategyTest {

    @Test
    public void testGetValue() throws VException {
        final IGetValueStrategy lStrategy = new CriteriumValueStrategy();

        KeyCriterion lCriterion = new KeyCriterionImpl(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, "Adam", "=", BinaryBooleanOperator.AND, KeyCriterionImpl.COLUMN_MODIFIER_DFT);
        assertEquals("'Adam'", lStrategy.getValue(lCriterion));

        lCriterion = new KeyCriterionImpl(Test2DomainObjectHomeImpl.KEY_AMOUNT, new Integer(98), "=", BinaryBooleanOperator.AND, KeyCriterionImpl.COLUMN_MODIFIER_DFT);
        assertEquals("98", lStrategy.getValue(lCriterion));
    }

}
