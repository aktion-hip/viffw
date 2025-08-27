package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.util.ListJoiner;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 */
public class ListJoinerTest {

    @Test
    void testJoin() {

        String lExpected = "name, firstname, zip, street";
        ListJoiner lJoiner = new ListJoiner();
        lJoiner.addEntry("name");
        lJoiner.addEntry("firstname");
        lJoiner.addEntry("zip");
        lJoiner.addEntry("street");
        assertEquals(lExpected, lJoiner.joinSpaced(","));

        lExpected = "name, firstname, zip DESC, street";
        lJoiner = new ListJoiner();
        lJoiner.addEntry("name");
        lJoiner.addEntry("firstname");
        lJoiner.addEntry("zip", "%1$s %2$s", "DESC");
        lJoiner.addEntry("street");
        assertEquals(lExpected, lJoiner.joinSpaced(","));

        lExpected = "name, firstname";
        lJoiner = new ListJoiner();
        lJoiner.addEntry("name");
        lJoiner.addEntry("firstname");
        lJoiner.addEntry("", "%1$s %2$s", "DESC");
        lJoiner.addEntry("");
        assertEquals(lExpected, lJoiner.joinSpaced(","));

        lExpected = "name, firstname, street";
        lJoiner = new ListJoiner();
        lJoiner.addEntry("name");
        lJoiner.addEntry("firstname");
        lJoiner.addEntry("", "%1$s %2$s", "DESC");
        lJoiner.addEntry("street");
        assertEquals(lExpected, lJoiner.joinSpaced(","));
    }

}
