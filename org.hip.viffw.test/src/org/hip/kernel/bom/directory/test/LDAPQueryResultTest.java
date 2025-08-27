package org.hip.kernel.bom.directory.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.naming.NamingException;

import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.directory.DirContextWrapper;
import org.hip.kernel.bom.directory.LDAPQueryResult;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.Test;

/** @author Luthiger Created: 06.07.2007 */
public class LDAPQueryResultTest {
    private final static String NL = System.getProperty("line.separator");
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
    "Output: cn=tenth, uid=1010, name=Tenth, sn=Tenth's sn, mail=tenth@my.org" };

    @Test
    void testDo() throws NamingException, VException, SQLException {
        final DirContextWrapper lContext = new TestDirContext();
        final LDAPQueryResult lResult = new LDAPQueryResult(new TestLDAPObjectHome(),
                lContext.search("(cn=test)", null), EXPECTED.length, null);

        final Collection<String> lRetrieved = new Vector<String>();
        while (lResult.hasMoreElements()) {
            final GeneralDomainObject lModel = lResult.next();
            lRetrieved.add(getContent(lModel));
        }

        assertEquals(EXPECTED.length, lRetrieved.size());
        for (int i = 0; i < EXPECTED.length; i++) {
            assertTrue(lRetrieved.contains(EXPECTED[i]));
        }
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
    void testDoAsXML() throws NamingException, VException, SQLException {
        final String lExpected = "<Member>" + NL +
                "    <propertySet>" + NL +
                "        <Firstname>First's sn</Firstname>" + NL +
                "        <Mail>first@my.org</Mail>" + NL +
                "        <UserID>111</UserID>" + NL +
                "        <ID>first</ID>" + NL +
                "        <Name>First</Name>" + NL +
                "    </propertySet>" + NL +
                "</Member>";

        final DirContextWrapper lContext = new TestDirContext();
        final LDAPQueryResult lResult = new LDAPQueryResult(new TestLDAPObjectHome(),
                lContext.search("(cn=test)", null), EXPECTED.length, null);

        final StringBuffer lTest = new StringBuffer();
        while (lResult.hasMoreElements()) {
            lTest.append(lResult.nextAsXMLString());
        }

        assertTrue(lTest.indexOf(lExpected) >= 0);
    }

}
