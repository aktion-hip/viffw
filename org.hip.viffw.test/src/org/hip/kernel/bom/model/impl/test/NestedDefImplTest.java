package org.hip.kernel.bom.model.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.model.GroupingDef;
import org.hip.kernel.bom.model.NestedDef;
import org.hip.kernel.bom.model.impl.GroupingDefImpl;
import org.hip.kernel.bom.model.impl.NestedDefImpl;
import org.hip.kernel.util.DefaultNameValueList;
import org.hip.kernel.util.NameValueList;
import org.hip.kernel.util.VNameValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 */
public class NestedDefImplTest {
    NestedDef nestedDef = null;

    @BeforeEach
    void setUp() throws Exception {
        final String[][] lAttr1 = {{"columnName", "GroupID"}, {"domainObject", "org.hip.kernel.bom.impl.test.TestParticipantImpl"}};
        final String[][] lAttr2 = {{"columnName", "MemberID"}, {"as", "Registered"}, {"modifier", "COUNT"}, {"domainObject", "org.hip.kernel.bom.impl.test.TestParticipantImpl"}};

        this.nestedDef = new NestedDefImpl("test");
        this.nestedDef.addColumnDef(createColumns(lAttr1));
        this.nestedDef.addColumnDef(createColumns(lAttr2));
    }

    private NameValueList createColumns(final String[][] inAttributes) throws VNameValueException {
        final NameValueList outList = new DefaultNameValueList();
        for (int i = 0; i < inAttributes.length; i++) {
            outList.setValue(inAttributes[i][0], inAttributes[i][1]);
        }
        return outList;
    }

    @Test
    void testGetNestedQuery() {
        final String lExpected = "(SELECT tblParticipant.GROUPID, COUNT(tblParticipant.MEMBERID) AS Registered FROM tblParticipant GROUP BY tblParticipant.GROUPID) test";
        try {
            final GroupingDef lGroupingDef = new GroupingDefImpl("GROUP");
            final String[][] lAttr = {{"columnName", "GroupID"}, {"domainObject", "org.hip.kernel.bom.impl.test.TestParticipantImpl"}};
            lGroupingDef.addColumnDef(createColumns(lAttr));
            this.nestedDef.addGroupingDef(lGroupingDef);

            assertEquals(lExpected, this.nestedDef.getNestedQuery());
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    void testGetSQLColumnName() {
        final String lExpected = "GROUPID";
        try {
            final String[][] lAttr = {{"columnName", "GroupID"}, {"alias", "Test"}, {"domainObject", "org.hip.kernel.bom.impl.test.TestParticipantImpl"}};
            this.nestedDef.addColumnDef(createColumns(lAttr));
            assertEquals(lExpected, this.nestedDef.getSQLColumnName("Test"));
            assertEquals(lExpected, this.nestedDef.getSQLColumnName("GroupID"));
        }
        catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }
}
