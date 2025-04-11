package org.hip.kernel.sys.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.sys.Assert;
import org.hip.kernel.sys.AssertionFailedError;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class AssertTest {

    @Test
    public void testAssert() {
        assertTrue(Assert.assertTrue(Assert.ERROR_OUT, this, "testAssert", 2 > 0) == Assert.NO_FAILURE);
        assertTrue(Assert.assertTrue(Assert.ERROR_OUT, this, "testAssert", 0 > 2) == Assert.FAILURE);
        assertTrue(Assert.assertNotNull(Assert.ERROR_OUT, this, "testAssert", this) == Assert.NO_FAILURE);
        assertTrue(Assert.assertNotNull(Assert.ERROR_OUT, this, "testAssert", null) == Assert.FAILURE);

        AssertionFailedError lError = null;
        try {
            assertTrue(Assert.assertNotNull(Assert.ERROR, this, "testAssert", null) == Assert.FAILURE);
        }
        catch (final AssertionFailedError err) {
            lError = err;
        }
        assertNotNull(lError);
    }
}
