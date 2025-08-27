package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.XMLSerializer;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 * Created on Dec 24, 2003
 */
public class NestedJoinTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromGroup();
        DataHouseKeeper.INSTANCE.deleteAllFromParticipant();
    }

    @Test
    public void testNested() throws VException, SQLException {
        //create 2 groups
        final DomainObject lGroup = DataHouseKeeper.INSTANCE.getGroupHome().create();
        lGroup.set(TestGroupHomeImpl.KEY_NAME, "test1");
        lGroup.set(TestGroupHomeImpl.KEY_DESCRIPTION, "Test group 1 for testing nested queries.");
        lGroup.insert(true);
        final BigDecimal lGroupID1 = DataHouseKeeper.INSTANCE.getGroupHome().getMax(TestGroupHomeImpl.KEY_ID);
        lGroup.setVirgin();
        lGroup.set(TestGroupHomeImpl.KEY_NAME, "test2");
        lGroup.set(TestGroupHomeImpl.KEY_DESCRIPTION, "Test group 2 for testing nested queries.");
        lGroup.insert(true);
        final BigDecimal lGroupID2 = DataHouseKeeper.INSTANCE.getGroupHome().getMax(TestGroupHomeImpl.KEY_ID);

        //add 6 participants to group1 and 9 participants to group2
        final DomainObject lParticipant = DataHouseKeeper.INSTANCE.getParticipantHome().create();
        for (int i = 1; i < 7 ; i++) {
            lParticipant.set(TestParticipantHomeImpl.KEY_GROUPID, lGroupID1);
            lParticipant.set(TestParticipantHomeImpl.KEY_MEMBERID, new Integer(i));
            lParticipant.insert(true);
            lParticipant.setVirgin();
        }
        for (int i = 1; i < 10; i++) {
            lParticipant.set(TestParticipantHomeImpl.KEY_GROUPID, lGroupID2);
            lParticipant.set(TestParticipantHomeImpl.KEY_MEMBERID, new Integer(10 + i));
            lParticipant.insert(true);
            lParticipant.setVirgin();
        }

        final XMLSerializer lSerializer = new XMLSerializer();
        final QueryResult lResult = DataHouseKeeper.INSTANCE.getNestedDomainObjectHomeImpl().select();
        int lCount = 0;
        int lRegistered = 0;
        while (lResult.hasMoreElements()) {
            final GeneralDomainObject lDomainObject = lResult.nextAsDomainObject();
            lRegistered += ((BigDecimal)lDomainObject.get(TestNestedDomainObjectHomeImpl.KEY_REGISTERED)).intValue();
            lDomainObject.accept(lSerializer);
            lCount++;
        }
        assertEquals(2, lCount);
        assertEquals(15, lRegistered);
        System.out.println(lSerializer.toString());
    }
}
