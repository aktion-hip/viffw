package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.PlacefillerCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** @author Benno Luthiger Created on Nov 8, 2004 */
public class PlaceholderHomeTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() {
        String lExpected = "";
        if (DataHouseKeeper.INSTANCE.isDBMySQL()) {
            lExpected = "SELECT tblTest.TESTID, tblTest.SNAME, tblTest.SFIRSTNAME, tblTest.DTMUTATION FROM tblTest LEFT JOIN (SELECT tblTestMember.TESTMEMBERID, tblTestMember.SFIRSTNAME, tblTestMember.DTMUTATION, tblTestMember.SNAME, tblTestMember.SPASSWORD FROM tblTestMember WHERE tblTestMember.SNAME = 'Foo') AS Admins ON tblTest.SNAME = Admins.SNAME WHERE tblTest.TESTID = 69";
        } else if (DataHouseKeeper.INSTANCE.isDBOracle()) {
            lExpected = "";
        }
        try {
            final TestJoinWithPlaceholderHome lPlaceholderHome = new TestJoinWithPlaceholderHome();

            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(TestDomainObjectHomeImpl.KEY_NAME, "Foo");
            final PlacefillerCollection lPlacefillers = new PlacefillerCollection();
            lPlacefillers.add(new TestDomainObjectHomeImpl(), lKey, TestJoinWithPlaceholderHome.NESTED_ALIAS);

            lKey = new KeyObjectImpl();
            lKey.setValue(Test2DomainObjectHomeImpl.KEY_ID, new Integer(69));

            lPlaceholderHome.select(lKey, lPlacefillers);
            assertEquals(lExpected, lPlaceholderHome.getTestObjects().next());
        } catch (final Exception exc) {
            fail(exc.getMessage());
        }
    }
}
