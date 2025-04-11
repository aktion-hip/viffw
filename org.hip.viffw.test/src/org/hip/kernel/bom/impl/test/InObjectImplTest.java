/**
 *
 */
package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.bom.impl.InObjectImpl;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luthiger
 * Created: 14.10.2006
 */
public class InObjectImplTest {

    /**
     * Test method for {@link org.hip.kernel.bom.impl.InObjectImpl#toString()}.
     */
    @Test
    public void testToString() {
        final InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("in 1", "IN (0)", lInObject1.toString());

        final InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(new Integer[] {new Integer(14), new Integer(15), new Integer(22)});
        assertEquals("in 2", "IN (14, 15, 22)", lInObject2.toString());

        final InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] {"Hallo", "14", "15", "22", "Adam"});
        assertEquals("in 3", "IN ('Hallo', '14', '15', '22', 'Adam')", lInObject3.toString());
    }

    @Test
    public void testToPrepared() {
        final InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("in 1", "IN ()", lInObject1.toPrepared());

        final InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(new Integer[] {new Integer(14), new Integer(15), new Integer(22)});
        assertEquals("in 2", "IN (?, ?, ?)", lInObject2.toPrepared());

        final InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] {"Hallo", "14", "15", "22", "Adam"});
        assertEquals("in 3", "IN (?, ?, ?, ?, ?)", lInObject3.toPrepared());
    }

}
