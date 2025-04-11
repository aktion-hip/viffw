package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Hashtable;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.impl.AbstractContext;
import org.hip.kernel.servlet.impl.ServletRequestHelper;
import org.hip.kernel.servlet.test.TestServletRequest.Builder;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Luthiger
 * Created: 23.09.2010
 */
public class ServletRequestHelperTest {

    @Test
    public void testClassic() throws Exception {
        final String[] lExpected = new String[] {"blue", "green", "red"};

        final Builder lBuilder = new TestServletRequest.Builder();
        final Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>();
        lParameters.put("first", new String[] {"done"});
        lParameters.put("second", new String[] {"again"});
        lParameters.put("multi", lExpected);
        lParameters.put(AbstractContext.REQUEST_TYPE, new String[] {"doTest"});

        final HttpServletRequest lRequest = lBuilder.setMethod("POST").setParameters(lParameters).build();
        final ServletRequestHelper lHelper = new ServletRequestHelper(lRequest);
        final Context lContext = new TestContext();
        lHelper.addParametersToContext(lContext, AbstractContext.REQUEST_TYPE);

        //inspect helper
        assertTrue(lHelper.containsParameter(AbstractContext.REQUEST_TYPE));
        assertEquals("doTest", lHelper.getParameterValue(AbstractContext.REQUEST_TYPE));
        assertTrue(lHelper.containsParameter("first"));
        assertEquals("done", lHelper.getParameterValue("first"));
        assertFalse(lHelper.containsParameter("something"));
        assertNull(lHelper.getParameterValue("something"));

        //inspect context
        assertEquals("done", lContext.getParameterValue("first"));
        assertEquals("", lContext.getParameterValue("something"));
        assertEquals("", lContext.getParameterValue(AbstractContext.REQUEST_TYPE));

        final String[] lValueArray = lContext.getParameterValueArray("multi");
        assertEquals(lExpected.length, lValueArray.length);
        for (int i = 0; i < lExpected.length; i++) {
            assertEquals("array value "+i, lExpected[i], lValueArray[i]);
        }
    }

    //To test this, we must define multipart boundaries.
    //exc: 'the request was rejected because no multipart boundary was found'
    //	public void testMultipart() throws Exception {
    //		String[] lExpected = new String[] {"blue", "green", "red"};
    //
    //		Builder lBuilder = new TestServletRequest.Builder();
    //		Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>();
    //		lParameters.put("first", new String[] {"done"});
    //		lParameters.put("second", new String[] {"again"});
    //		lParameters.put("multi", lExpected);
    //		lParameters.put(AbstractContext.REQUEST_TYPE, new String[] {"doTest"});
    //
    //		HttpServletRequest lRequest = lBuilder.setMethod("POST").setParameters(lParameters).setContentType("multipart/form-data").build();
    //		ServletRequestHelper lHelper = new ServletRequestHelper(lRequest);
    //		Context lContext = new TestContext();
    //		lHelper.addParametersToContext(lContext, AbstractContext.REQUEST_TYPE);
    //
    //		//inspect helper
    // assertTrue( lHelper.containsParameter(AbstractContext.REQUEST_TYPE));
    // assertEquals( "doTest", lHelper.getParameterValue(AbstractContext.REQUEST_TYPE));
    // assertTrue( lHelper.containsParameter("first"));
    // assertEquals( "done", lHelper.getParameterValue("first"));
    // assertFalse( lHelper.containsParameter("something"));
    // assertNull( lHelper.getParameterValue("something"));
    //	}

}
