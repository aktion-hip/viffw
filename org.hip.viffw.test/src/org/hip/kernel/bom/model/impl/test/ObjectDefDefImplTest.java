package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import org.hip.kernel.bom.PropertySet;
import org.hip.kernel.bom.impl.PropertySetImpl;
import org.hip.kernel.bom.model.MetaModelHome;
import org.hip.kernel.bom.model.ObjectDefDef;
import org.hip.kernel.bom.model.impl.ObjectDefDefImpl;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class ObjectDefDefImplTest {

    @Test
    public void testCreation() {
        final String[] lExpected = {ObjectDefDef.version, ObjectDefDef.propertyDefs, ObjectDefDef.keyDefs, ObjectDefDef.parent, ObjectDefDef.objectName, ObjectDefDef.baseDir};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));

        final ObjectDefDef lDef = MetaModelHome.singleton.getObjectDefDef() ;
        assertNotNull(lDef);

        final int i = 0;
        for (final String lName : lDef.getPropertyNames2()) {
            assertTrue(lVExpected.contains(lName));
        }
    }

    @Test
    public void testInitializePropertySet() {

        final String[] lExpected = {ObjectDefDef.version, ObjectDefDef.propertyDefs, ObjectDefDef.keyDefs, ObjectDefDef.parent, ObjectDefDef.objectName, ObjectDefDef.baseDir};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));

        final PropertySet lPropertySet = new PropertySetImpl(null);
        final ObjectDefDef lDef = MetaModelHome.singleton.getObjectDefDef();
        ((ObjectDefDefImpl)lDef).initializePropertySet(lPropertySet);

        final int i = 0;
        for (final String lName : lPropertySet.getNames2()) {
            assertTrue(lVExpected.contains(lName));
        }

        final String lTest1 = "";
        try {
            if (Class.forName("java.lang.String").isInstance(lTest1)) {
                System.out.println("ok");
            }
            if (Class.forName("org.hip.kernel.bom.model.ObjectDefDef").isInstance(lTest1)) {
                System.out.println("falsch");
            }
            if (Class.forName("org.hip.kernel.bom.model.ObjectDefDef").isInstance(lDef)) {
                System.out.println("ok");
            }
        }
        catch (final ClassNotFoundException exc) {}
    }
}
