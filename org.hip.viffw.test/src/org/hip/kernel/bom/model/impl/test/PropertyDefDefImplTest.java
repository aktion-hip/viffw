package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.hip.kernel.bom.model.MetaModelHome;
import org.hip.kernel.bom.model.PropertyDefDef;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class PropertyDefDefImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreation() {

        final String[] lExpected = {"propertyType", "valueType", "propertyName", "mappingDef", "relationshipDef", "formatPattern"};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));
        final PropertyDefDef lDef = MetaModelHome.singleton.getPropertyDefDef() ;
        assertNotNull(lDef);

        final int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            assertTrue(lVExpected.contains(lNames.next()));
        }
    }
}
