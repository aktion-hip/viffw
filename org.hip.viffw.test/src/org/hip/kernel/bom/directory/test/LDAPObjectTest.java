package org.hip.kernel.bom.directory.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.hip.kernel.bom.BOMException;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyCriterion;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.directory.LDAPObject;
import org.hip.kernel.bom.directory.LDAPObjectHome;
import org.hip.kernel.bom.impl.XMLSerializer;
import org.hip.kernel.util.SortableItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** @author Luthiger Created: 21.07.2007 */
public class LDAPObjectTest {
    private final static String NL = System.getProperty("line.separator");
    private final static String SERIALIZED = NL +
            "<Member>" + NL +
            "    <propertySet>" + NL +
            "        <Firstname>First's sn</Firstname>" + NL +
            "        <Mail>first@my.org</Mail>" + NL +
            "        <UserID>111</UserID>" + NL +
            "        <ID>first</ID>" + NL +
            "        <Name>First</Name>" + NL +
            "    </propertySet>" + NL +
            "</Member>";

    private GeneralDomainObject model;

    @BeforeEach
    public void setUp() throws Exception {
        final LDAPObjectHome lHome = new TestLDAPObjectHome();
        final QueryResult lResult = lHome.select();
        this.model = lResult.next();
    }

    @Test
    public void testDo() {
        assertTrue(this.model instanceof LDAPObject);
        assertEquals("Member", this.model.getObjectName());
    }

    @Test
    public void testAccecpt() throws BOMException, SQLException {
        final XMLSerializer lSerializer = new XMLSerializer();
        this.model.accept(lSerializer);
        assertEquals(SERIALIZED, lSerializer.toString());
    }

    @Test
    public void testGetKey() {
        final KeyObject lKey = this.model.getKey();
        lKey.getItems2();
        for (final SortableItem lItem : lKey.getItems2()) {
            final KeyCriterion lCriterion = (KeyCriterion) lItem;
            assertEquals("ID", lCriterion.getName());
            assertEquals("first", lCriterion.getValue());
        }
    }

}
