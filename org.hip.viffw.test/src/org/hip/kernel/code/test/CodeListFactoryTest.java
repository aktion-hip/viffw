package org.hip.kernel.code.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.code.CodeList;
import org.hip.kernel.code.CodeListFactory;
import org.hip.kernel.code.CodeNotFoundException;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class CodeListFactoryTest {

    @Test
    public void testDo() throws Exception {
        final CodeList lCodeList = new CodeListFactory().createCodeList("CodeTest", "de");
        assertNotNull(lCodeList);

        assertTrue(lCodeList.existElementID("1"));
        assertTrue(lCodeList.existElementID("2"));
        assertTrue(lCodeList.existElementID("3"));
        assertTrue(lCodeList.existElementID("4"));

        try {
            lCodeList.existElementID("5");
            fail("ID 5 doesn't exist");
        }
        catch (final CodeNotFoundException exc) {
        }
    }

}
