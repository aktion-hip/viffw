package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.bom.Home;
import org.hip.kernel.bom.model.MetaModelHome;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class MetaModelHomeImplTest {
    public static MetaModelHome	home = null ;

    @BeforeAll
    public static void init() {
        home = (MetaModelHome) Home.manager.getHome("org.hip.kernel.bom.model.impl.MetaModelHomeImpl") ;
    }

    @Test
    public void testGetDefDefs() {
        // Test if all DefDefs will be instantiated
        assertNotNull(home.getObjectDefDef());
        assertNotNull(home.getPropertyDefDef());
        assertNotNull(home.getMappingDefDef());
        assertNotNull(home.getJoinDefDef());
        assertNotNull(home.getJoinedObjectDefDef());
        assertNotNull(home.getKeyDefDef());
        assertNotNull(home.getRelationshipDefDef());

        assertTrue(home.getObjectDefDef() instanceof org.hip.kernel.bom.model.ObjectDefDef);
        assertTrue(home.getPropertyDefDef() instanceof org.hip.kernel.bom.model.PropertyDefDef);
        assertTrue(home.getMappingDefDef() instanceof org.hip.kernel.bom.model.MappingDefDef);
        assertTrue(home.getJoinDefDef() instanceof org.hip.kernel.bom.model.JoinDefDef);
        assertTrue(home.getJoinedObjectDefDef() instanceof org.hip.kernel.bom.model.JoinedObjectDefDef);
        assertTrue(home.getKeyDefDef() instanceof org.hip.kernel.bom.model.KeyDefDef);
        assertTrue(home.getRelationshipDefDef() instanceof org.hip.kernel.bom.model.RelationshipDefDef);
    }

    @Test
    public void testInstantiation() {
        assertNotNull(home);
    }
}
