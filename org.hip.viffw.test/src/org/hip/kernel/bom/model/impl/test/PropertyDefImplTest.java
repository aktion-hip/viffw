package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.hip.kernel.bom.model.MappingDef;
import org.hip.kernel.bom.model.MappingDefDef;
import org.hip.kernel.bom.model.PropertyDef;
import org.hip.kernel.bom.model.PropertyDefDef;
import org.hip.kernel.bom.model.TypeDef;
import org.hip.kernel.bom.model.impl.MappingDefImpl;
import org.hip.kernel.bom.model.impl.PropertyDefImpl;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class PropertyDefImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreation() {

        final String[] lExpected = {"propertyType", "valueType", "propertyName", "mappingDef", "formatPattern", "relationshipDef"};
        final Vector<String> lVExpected = new Vector<String>(Arrays.asList(lExpected));

        final PropertyDef lDef = new PropertyDefImpl() ;
        assertNotNull(lDef);

        final int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext();) {
            assertTrue(lVExpected.contains(lNames.next()));
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreationWithInitialValues() {

        final Object[][] lInitValues = {
                { "propertyName"	, "firstName"					}
                ,	{ "propertyType"	, "simple"						}
                ,	{ "valueType"		, "java.lang.String" 			}
        } ;

        final PropertyDef lDef = new PropertyDefImpl( lInitValues ) ;
        assertNotNull(lDef);

        int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            final String lName = (String) lNames.next();
            try {
                i++;
                VSys.out.println( "name=" + lName + " value=" + lDef.get( lName )  ) ;
            }
            catch ( final Exception exc ) {
                VSys.err.println( exc ) ;
            }
        }
        assertEquals(6, i);
    }

    @Test
    public void testEquals() {

        final PropertyDef lDef1 = new PropertyDefImpl();
        final PropertyDef lDef2 = new PropertyDefImpl();
        final PropertyDef lDef3 = new PropertyDefImpl();
        assertNotNull(lDef1);
        assertNotNull(lDef2);
        assertNotNull(lDef3);

        final String[] lExpected1 = {"PERSON", "NAME"};
        final String[] lExpected2 = {"PERSON", "VORNAME"};
        final Object[][] lInitValues1 = {
                { MappingDefDef.tableName	, lExpected1[0]	}
                ,	{ MappingDefDef.columnName	, lExpected1[1]	}
        } ;
        final Object[][] lInitValues2 = {
                { MappingDefDef.tableName	, lExpected2[0]	}
                ,	{ MappingDefDef.columnName	, lExpected2[1]	}
        } ;
        final MappingDef lMappingDef1 = new MappingDefImpl(lInitValues1);
        final MappingDef lMappingDef2 = new MappingDefImpl(lInitValues2);

        try {
            lDef1.set(PropertyDefDef.propertyName, "ID");
            lDef1.set(PropertyDefDef.propertyType, "simple");
            lDef1.set(PropertyDefDef.valueType, "Number");

            lDef2.set(PropertyDefDef.propertyName, "Name");
            lDef2.set(PropertyDefDef.propertyType, "simple");
            lDef2.set(PropertyDefDef.valueType, "String");

            lDef3.set(PropertyDefDef.propertyName, "ID");
            lDef3.set(PropertyDefDef.propertyType, "simple");
            lDef3.set(PropertyDefDef.valueType, "Number");

            assertTrue(lDef1.equals(lDef3));
            assertEquals(lDef1.hashCode(), lDef3.hashCode());
            assertTrue(!lDef1.equals(lDef2));
            assertTrue(lDef1.hashCode() != lDef2.hashCode());

            lDef1.set(PropertyDefDef.mappingDef, lMappingDef1);
            lDef3.set(PropertyDefDef.mappingDef, lMappingDef1);

            assertTrue(lDef1.equals(lDef3));
            assertEquals(lDef1.hashCode(), lDef3.hashCode());

            lDef3.set(PropertyDefDef.mappingDef, lMappingDef2);
            assertTrue(!lDef1.equals(lDef3));
            assertTrue(lDef1.hashCode() != lDef3.hashCode());
        }
        catch (final org.hip.kernel.bom.SettingException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetValueClassType() {

        final PropertyDef lDef = new PropertyDefImpl();
        try {
            lDef.set(PropertyDefDef.valueType, TypeDef.String);
            assertEquals("java.lang.String", lDef.getValueClassType());

            lDef.set(PropertyDefDef.valueType, TypeDef.Number);
            assertEquals("java.lang.Number", lDef.getValueClassType());

            lDef.set(PropertyDefDef.valueType, TypeDef.Timestamp);
            assertEquals("java.sql.Timestamp", lDef.getValueClassType());

            lDef.set(PropertyDefDef.valueType, TypeDef.Date);
            assertEquals("java.sql.Date", lDef.getValueClassType());
        }
        catch (final org.hip.kernel.bom.SettingException exc) {
            fail("Setting of ValueType");
        }
    }

    @Test
    public void testToString() {
        final PropertyDef lDef = new PropertyDefImpl();
        assertNotNull(lDef);

        try {
            lDef.set(PropertyDefDef.propertyName, "ID");
            lDef.set(PropertyDefDef.propertyType, "simple");
            lDef.set(PropertyDefDef.valueType, "Number");

            assertEquals(
                    "< org.hip.kernel.bom.model.impl.PropertyDefImpl propertyName=\"ID\" valueType=\"Number\" propertyType=\"simple\" />",
                    lDef.toString());
        }
        catch (final org.hip.kernel.bom.SettingException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testClone() throws Exception {
        final PropertyDef lDef1 = new PropertyDefImpl();
        assertNotNull(lDef1);

        lDef1.set(PropertyDefDef.propertyName, "ID1");
        lDef1.set(PropertyDefDef.propertyType, "simple");
        lDef1.set(PropertyDefDef.valueType, "Number");

        final PropertyDef lDef2 = (PropertyDef)((PropertyDefImpl)lDef1).clone();
        assertTrue(lDef1.equals(lDef2));

        //PropertyDefs are independent
        lDef2.set(PropertyDefDef.propertyName, "ID2");
        assertTrue(!lDef1.equals(lDef2));
    }
}
