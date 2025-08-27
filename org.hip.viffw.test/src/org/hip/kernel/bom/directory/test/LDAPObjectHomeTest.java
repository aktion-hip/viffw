package org.hip.kernel.bom.directory.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.LimitObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.directory.LDAPObjectHome;
import org.hip.kernel.bom.directory.LDAPQueryResult;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.LimitObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 * Created: 15.07.2007
 */
public class LDAPObjectHomeTest {
    private final static String[] EXPECTED = new String[] {
            "Output: cn=first, uid=111, name=First, sn=First's sn, mail=first@my.org",
            "Output: cn=second, uid=2222, name=Second, sn=Second's sn, mail=second@my.org",
            "Output: cn=third, uid=333333, name=Third, sn=Third's sn, mail=third@my.org",
            "Output: cn=forth, uid=4, name=Forth, sn=Forth's sn, mail=forth@my.org",
            "Output: cn=fith, uid=5, name=Fith, sn=Fith's sn, mail=fith@my.org",
            "Output: cn=sixth, uid=6, name=Sixth, sn=Sixth's sn, mail=sixth@my.org",
            "Output: cn=seventh, uid=7, name=Seventh, sn=Seventh's sn, mail=seventh@my.org",
            "Output: cn=eighth, uid=8, name=Eighth, sn=Eighth's sn, mail=eighth@my.org",
            "Output: cn=ninth, uid=9, name=Ninth, sn=Ninth's sn, mail=ninth@my.org",
    "Output: cn=tenth, uid=1010, name=Tenth, sn=Tenth's sn, mail=tenth@my.org"};

    @Test
    public void testSelect() throws VException, SQLException {
        final String lQueryTerm = "query";

        final LDAPObjectHome lHome = new TestLDAPObjectHome();
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(TestLDAPObjectHome.KEY_NAME, lQueryTerm + "*");
        lKey.setValue(TestLDAPObjectHome.KEY_FIRST_NAME, lQueryTerm + "*", "=", BinaryBooleanOperator.OR);

        QueryResult lResult = lHome.select(lKey);

        final Collection<String> lRetrieved = new Vector<String>();
        while (lResult.hasMoreElements()) {
            lRetrieved.add(getContent(lResult.next()));
        }
        assertEquals(EXPECTED.length, lRetrieved.size());
        for (int i = 0; i < EXPECTED.length; i++) {
            assertTrue(lRetrieved.contains(EXPECTED[i]));
        }

        //		set limit
        final int lExpectedLimit = 6;
        final LimitObject lLimit = new LimitObjectImpl(6);
        lResult = lHome.select(lKey, lLimit);
        int i = 0;
        while (lResult.hasMoreElements()) {
            lResult.next();
            i++;
        }
        assertEquals(lExpectedLimit, i);
        assertEquals(lExpectedLimit, ((LDAPQueryResult) lResult).getCount());

    }

    private String getContent(final GeneralDomainObject inModel) throws VException {
        return String.format("Output: cn=%s, uid=%s, name=%s, sn=%s, mail=%s",
                inModel.get(TestLDAPObjectHome.KEY_ID),
                inModel.get(TestLDAPObjectHome.KEY_USER_ID),
                inModel.get(TestLDAPObjectHome.KEY_NAME),
                inModel.get(TestLDAPObjectHome.KEY_FIRST_NAME),
                inModel.get(TestLDAPObjectHome.KEY_MAIL));
    }

    @Test
    public void testFindByKey() throws VException {
        final String lQueryTerm = "query";

        final LDAPObjectHome lHome = new TestLDAPObjectHome();
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(TestLDAPObjectHome.KEY_NAME, lQueryTerm + "*");
        lKey.setValue(TestLDAPObjectHome.KEY_FIRST_NAME, lQueryTerm + "*", "=", BinaryBooleanOperator.OR);

        final DomainObject lModel = lHome.findByKey(lKey);
        assertEquals("first", lModel.get(TestLDAPObjectHome.KEY_ID));
        assertEquals("111", lModel.get(TestLDAPObjectHome.KEY_USER_ID));
    }

}
