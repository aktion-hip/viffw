package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.hip.kernel.bom.model.MappingDef;
import org.hip.kernel.bom.model.MappingDefDef;
import org.hip.kernel.bom.model.impl.MappingDefImpl;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class MappingDefImplTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testCreation() {
        final String[] lExpected = {"tableName", "columnName"};

        final MappingDef lDef = new MappingDefImpl() ;
        assertNotNull(lDef);

        int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            assertEquals(lExpected[i++], lNames.next());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreationWithInitialValues() {
        final String[] lExpected = {"PERSON", "NAME"};

        final Object[][] lInitValues = {
                { "tableName"	, lExpected[0]	}
                ,	{ "columnName"	, lExpected[1]	}
        } ;

        final MappingDef lDef = new MappingDefImpl( lInitValues ) ;
        assertNotNull(lDef);

        int i = 0;
        for (final Iterator<?> lNames = lDef.getPropertyNames(); lNames.hasNext(); ) {
            final String lName = (String) lNames.next();
            try {
                assertEquals(lExpected[i++], lDef.get(lName));
            }
            catch ( final Exception exc ) {
                VSys.err.println( exc ) ;
            }
        }
    }

    @Test
    public void testEquals() {
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

        final MappingDef lDef1 = new MappingDefImpl(lInitValues1);
        final MappingDef lDef2 = new MappingDefImpl(lInitValues2);
        final MappingDef lDef3 = new MappingDefImpl(lInitValues1);

        assertTrue(lDef1.equals(lDef3));
        assertEquals(lDef1.hashCode(), lDef3.hashCode());
        assertTrue(!lDef1.equals(lDef2));
        assertTrue(lDef1.hashCode() != lDef2.hashCode());
    }

    @Test
    public void testToString() {
        final String[] lExpected = {"PERSON", "NAME"};

        final Object[][] lInitValues = {
                { MappingDefDef.tableName	, lExpected[0]	}
                ,	{ MappingDefDef.columnName	, lExpected[1]	}
        } ;

        final MappingDef lDef = new MappingDefImpl( lInitValues ) ;
        assertNotNull(lDef);
        assertEquals("< org.hip.kernel.bom.model.impl.MappingDefImpl tableName=\"PERSON\" columnName=\"NAME\" />",
                lDef.toString());
    }
}
