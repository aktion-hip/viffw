package org.hip.kernel.stext.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.stext.InlineStructuredText2HTML;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class InlineStructuredText2HTMLTest {

    @Test
    public void testConvertToHTML() {
        final String lInput1 = "*Bemerkung:* _LDAPUserFolder_ ben�tigt (*als LDAP-Client*) das *python-ldap Modul*. Damit dieses **mit _SSL_ arbeitet**, m�ssen die richtigen Bibliotheken eingebunden werden, was die *korrekte Installation* dieser Bibliotheken voraussetzt.";
        final String lExpected1 = "<em>Bemerkung:</em> <u>LDAPUserFolder</u> ben�tigt (<em>als LDAP-Client</em>) das <em>python-ldap Modul</em>. Damit dieses <strong>mit <u>SSL</u> arbeitet</strong>, m�ssen die richtigen Bibliotheken eingebunden werden, was die <em>korrekte Installation</em> dieser Bibliotheken voraussetzt.";

        final String lInput2 = "xx _this is html_ xx";
        final String lExpected2 = "xx <u>this is html</u> xx";

        final String lInput3 = "xx _this is html_";
        final String lExpected3 = "xx <u>this is html</u>";

        final String lInput4 = "xx *this is html* xx";
        final String lExpected4 = "xx <em>this is html</em> xx";

        final String lInput5 = "xx **this is html** xx";
        final String lExpected5 = "xx <strong>this is html</strong> xx";

        final String lInput6 = "<a href=\"index_html\">index_html</a>";
        final String lExpected6 = "<a href=\"index_html\">index_html</a>";

        final String lInput7 = "def __init__(self)";
        final String lExpected7 = "def __init__(self)";

        final String lInput8 = "this is '__a_literal__' eh";
        final String lExpected8 = "this is <code>__a_literal__</code> eh";

        final String lInput9 = "Note: structured_text is sometimes a night_mare";
        final String lExpected9 = "Note: structured_text is sometimes a night_mare";

        final String lInput10 = "xx 'this is literal' xx";
        final String lExpected10 = "xx <code>this is literal</code> xx";

        assertEquals(lExpected1, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput1)));
        assertEquals(lExpected2, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput2)));
        assertEquals(lExpected3, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput3)));
        assertEquals(lExpected4, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput4)));
        assertEquals(lExpected5, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput5)));
        assertEquals(lExpected6, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput6)));
        assertEquals(lExpected7, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput7)));
        assertEquals(lExpected8, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput8)));
        assertEquals(lExpected9, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput9)));
        assertEquals(lExpected10, new String(InlineStructuredText2HTML.getSingleton().convertToHTML(lInput10)));
    }

}
