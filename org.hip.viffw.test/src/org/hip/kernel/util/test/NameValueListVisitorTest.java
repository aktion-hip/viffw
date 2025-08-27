package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.exc.VError;
import org.hip.kernel.util.DefaultNameValue;
import org.hip.kernel.util.DefaultNameValueList;
import org.hip.kernel.util.DefaultNameValueListVisitor;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class NameValueListVisitorTest {

    @Test
    public void testVisit() {
        final DefaultNameValueList lList = new DefaultNameValueList();

        final DefaultNameValue lNameValue1 = new DefaultNameValue(lList, "testValue1", "Hallo Velo");
        DefaultNameValueListVisitor lVisitor = new DefaultNameValueListVisitor();
        lNameValue1.accept(lVisitor);
        final String lExpected1 = "<org.hip.kernel.util.DefaultNameValue name='testValue1'>\n" +
                "	< java.lang.String 'Hallo Velo' />\n" +
                "</org.hip.kernel.util.DefaultNameValue>";
        assertEquals(lExpected1, lVisitor.toString());

        final DefaultNameValue lNameValue2 = new DefaultNameValue(lList, "testValue2", null);
        lVisitor = new DefaultNameValueListVisitor();
        lNameValue2.accept(lVisitor);
        final String lExpected2 = "<org.hip.kernel.util.DefaultNameValue name='testValue2'>\n" +
                "	<value=?/>\n" +
                "</org.hip.kernel.util.DefaultNameValue>";
        assertEquals(lExpected2, lVisitor.toString());

        final DefaultNameValue lNameValue3 = new DefaultNameValue(lList, "testValue3", new VError("Hallo Error"));
        lVisitor = new DefaultNameValueListVisitor();
        lNameValue3.accept(lVisitor);
        final String lExpected3 = "<org.hip.kernel.util.DefaultNameValue name='testValue3'>\n" +
                "	< org.hip.kernel.exc.VError 'org.hip.kernel.exc.VError: Hallo Error' />\n" +
                "</org.hip.kernel.util.DefaultNameValue>";
        assertEquals(lExpected3, lVisitor.toString());

        lVisitor = new DefaultNameValueListVisitor();
        lList.add(lNameValue1);
        lList.add(lNameValue2);
        lList.add(lNameValue3);
        lList.accept(lVisitor);
        final String lExpectedV1 = "\t<org.hip.kernel.util.DefaultNameValue name='testValue1'>\n" +
                "\t\t< java.lang.String 'Hallo Velo' />\n" +
                "\t</org.hip.kernel.util.DefaultNameValue>";
        final String lExpectedV2 = "\t<org.hip.kernel.util.DefaultNameValue name='testValue2'>\n" +
                "\t\t<value=?/>\n" +
                "\t</org.hip.kernel.util.DefaultNameValue>";
        final String lExpectedV3 = "\t<org.hip.kernel.util.DefaultNameValue name='testValue3'>\n" +
                "\t\t< org.hip.kernel.exc.VError 'org.hip.kernel.exc.VError: Hallo Error' />\n" +
                "\t</org.hip.kernel.util.DefaultNameValue>";
        assertTrue(lVisitor.toString().indexOf(lExpectedV1) > 0);
        assertTrue(lVisitor.toString().indexOf(lExpectedV2) > 0);
        assertTrue(lVisitor.toString().indexOf(lExpectedV3) > 0);
    }
}
