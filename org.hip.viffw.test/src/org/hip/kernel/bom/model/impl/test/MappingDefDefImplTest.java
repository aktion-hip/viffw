package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.hip.kernel.bom.model.MappingDefDef;
import org.hip.kernel.bom.model.MetaModelHome;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class MappingDefDefImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreation() {
        final String[] lExpected = {"tableName", "columnName"};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));
        final MappingDefDef lDef = MetaModelHome.singleton.getMappingDefDef() ;
        assertNotNull(lDef);

        final int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            assertTrue(lVExpected.contains(lNames.next()));
        }
    }
}
