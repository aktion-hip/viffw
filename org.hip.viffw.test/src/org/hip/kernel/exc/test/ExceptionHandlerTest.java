package org.hip.kernel.exc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.exc.ExceptionHandler;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class ExceptionHandlerTest {

    @Test
    public void testConvert() {
        final String lMessage = "Test";
        final ExceptionHandler lExceptionHandler = TestExceptionHandler.instance();
        final Throwable lNewException = lExceptionHandler.convert(new ArrayIndexOutOfBoundsException(), lMessage);
        assertEquals(lMessage, lNewException.getMessage());
    }

    @Test
    public void testHandle() {
        final ExceptionHandler lExceptionHandler = TestExceptionHandler.instance();

        final int lExpected = ((TestExceptionHandler)lExceptionHandler).getNumberOfHandles();
        lExceptionHandler.handle(this, new ArrayIndexOutOfBoundsException());
        assertEquals(lExpected + 1, ((TestExceptionHandler) lExceptionHandler).getNumberOfHandles());

        lExceptionHandler.handle(new ArrayIndexOutOfBoundsException());
        assertEquals(lExpected + 2, ((TestExceptionHandler) lExceptionHandler).getNumberOfHandles());

        lExceptionHandler.handle(new ArrayIndexOutOfBoundsException(), true);
        assertEquals(lExpected + 3, ((TestExceptionHandler) lExceptionHandler).getNumberOfHandles());

        lExceptionHandler.handle(this, new ArrayIndexOutOfBoundsException(), true);
        assertEquals(lExpected + 4, ((TestExceptionHandler) lExceptionHandler).getNumberOfHandles());
    }
}
