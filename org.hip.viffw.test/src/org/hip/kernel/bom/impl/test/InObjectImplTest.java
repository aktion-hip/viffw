/**
 *
 */
package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.impl.InObjectImpl;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 */
public class InObjectImplTest {

    /**
     * Test method for {@link org.hip.kernel.bom.impl.InObjectImpl#toString()}.
     */
    @Test
    void testToString() {
        final InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("IN (0)", lInObject1.toString());

        final InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(
                new Integer[] { Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22) });
        assertEquals("IN (14, 15, 22)", lInObject2.toString());

        final InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] {"Hallo", "14", "15", "22", "Adam"});
        assertEquals("IN ('Hallo', '14', '15', '22', 'Adam')", lInObject3.toString());
    }

    @Test
    void testToPrepared() {
        final InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("IN ()", lInObject1.toPrepared());

        final InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(
                new Integer[] { Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22) });
        assertEquals("IN (?, ?, ?)", lInObject2.toPrepared());

        final InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] {"Hallo", "14", "15", "22", "Adam"});
        assertEquals("IN (?, ?, ?, ?, ?)", lInObject3.toPrepared());
    }

}
