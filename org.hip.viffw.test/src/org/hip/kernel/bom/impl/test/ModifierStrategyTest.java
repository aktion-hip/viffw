package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.impl.ModifierStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 * Created on 25.05.2006
 */
public class ModifierStrategyTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    /*
     * Test method for 'org.hip.kernel.bom.impl.ModifierStrategy.createModifiedSQL(DomainObjectHome)'
     */
    @Test
    public void testCreateModifiedSQL() {
        final DomainObjectHome lHome = DataHouseKeeper.INSTANCE.getSimpleHome();

        ModifierStrategy lStrategy = new ModifierStrategy("Double", ModifierStrategy.AVERAGE);
        assertEquals("AVG(tblTest.FDOUBLE)", new String(lStrategy.createModifiedSQL(lHome)));

        lStrategy = new ModifierStrategy("Double", ModifierStrategy.STDDEV);
        assertEquals("STDDEV(tblTest.FDOUBLE)", new String(lStrategy.createModifiedSQL(lHome)));

        lStrategy = new ModifierStrategy(new String[] {"Double", "Amount"}, ModifierStrategy.MIN);
        assertEquals("MIN(tblTest.FDOUBLE), MIN(tblTest.FAMOUNT)", new String(lStrategy.createModifiedSQL(lHome)));

        final int[] lModifiers = {ModifierStrategy.VARIANCE, ModifierStrategy.SUM};
        lStrategy = new ModifierStrategy(new String[] {"Double", "Amount"}, lModifiers);
        assertEquals("VARIANCE(tblTest.FDOUBLE), SUM(tblTest.FAMOUNT)", new String(lStrategy.createModifiedSQL(lHome)));
    }

}
