package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Hashtable;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.impl.AbstractContext;
import org.hip.kernel.servlet.test.TestServletRequest.Builder;
import org.hip.kernel.sys.AssertionFailedError;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author: Benno Luthiger
 */
public class RequestHandlerTest {
    private static final String NL = System.getProperty("line.separator");
    private final static String EXPECTED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
            "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
            "<title>VIF</title>\n</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + NL +
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "<firstLine>This is the first line.</firstLine>" + NL +
            "<secondLine>This line follows the first.</secondLine>" + NL +
            "</Root></transformed></body></html>" + NL;
    //	private final static String EXPECTED_ERR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
    //		"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
    //		"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" +
    //		"<title>VIF</title>\n</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + NL +
    //		"This is an error message in an error view!</br>\n" +
    //		"No request type defined!</br>\n" +
    //		"</body></html>" + NL;

    @Test
    public void testDoGet1() throws Exception {
        final TestRequestHandler lRequestHandler = new TestRequestHandler();

        TestServletResponse lResponse = new TestServletResponse();
        TestServletRequest.Builder lBuilder = new TestServletRequest.Builder();
        Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>(4);
        lParameters.put(AbstractContext.REQUEST_TYPE, new String[] {"testtask"});
        lParameters.put("name",new String[] { "VIF"});
        lParameters.put("version", new String[] {"1.0"});
        lParameters.put("state", new String[] {"beta"});
        final HttpServletRequest lRequest1 = lBuilder.setParameters(lParameters).build();
        lRequestHandler.doGet(lRequest1, lResponse);

        TestPrintWriter lWriter = (TestPrintWriter)lResponse.getWriter();
        assertEquals(EXPECTED, lWriter.getStreamedText());
        lWriter.close();
        lResponse = new TestServletResponse();

        //request without request type specified
        lBuilder = new TestServletRequest.Builder();
        lParameters = new Hashtable<String, String[]>(3);
        lParameters.put("name", new String[] {"VIF"});
        lParameters.put("version", new String[] {"1.0"});
        lParameters.put("state", new String[] {"beta"});
        final HttpServletRequest lRequest2 = lBuilder.setParameters(lParameters).build();
        lRequestHandler.doGet(lRequest2, lResponse);

        lWriter = (TestPrintWriter)lResponse.getWriter();
        assertEquals(EXPECTED, lWriter.getStreamedText());
        lWriter.close();
    }

    @Test
    public void testDoGet2() throws Exception {
        final TestRequestHandler lRequestHandler = new TestRequestHandler();
        final TestServletResponse lResponse = new TestServletResponse();

        try {
            lRequestHandler.doGet(null, lResponse);
            fail("shouldn't get here");
        }
        catch (final ServletException exc) {
            fail(exc.getMessage());
        }
        catch (final IOException exc) {
            fail(exc.getMessage());
        }
        catch (final Throwable t) {
            //left blank intentionally
        }
    }

    @Test
    public void testDoGet3() throws Exception {
        final TestRequestHandler lRequestHandler = new TestRequestHandler();
        final TestServletResponse lResponse = new TestServletResponse();
        try {
            //request which doesn't exist
            //we handled it by throwing an AssertionFailedError
            final Builder lBuilder = new TestServletRequest.Builder();
            final Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>(4);
            lParameters.put(AbstractContext.REQUEST_TYPE, new String[] {"test!!"});
            lParameters.put("name", new String[] {"VIF"});
            lParameters.put("version", new String[] {"1.0"});
            lParameters.put("state", new String[] {"beta"});
            final HttpServletRequest lRequest3 = lBuilder.setParameters(lParameters).build();
            lRequestHandler.doGet(lRequest3, lResponse);
            fail("shouldn't get here");
        }
        catch (final ServletException exc) {
            fail(exc.getMessage());
        }
        catch (final IOException exc) {
            fail(exc.getMessage());
        }
        catch (final AssertionFailedError exc) {
            //left blank intentionally
        }
    }

    @Test
    public void testGetContext() {
        final TestRequestHandler lRequestHandler = new TestRequestHandler();
        assertEquals(TestContext.class.getName(), lRequestHandler.getContextClassName());

        final Builder lBuilder = new TestServletRequest.Builder();
        final Hashtable<String, String[]> lParameters = new Hashtable<String, String[]>(3);
        lParameters.put("name", new String[] {"VIF"});
        lParameters.put("version", new String[] {"1.0"});
        lParameters.put("state", new String[] {"beta"});
        final HttpServletRequest lRequest = lBuilder.setParameters(lParameters).build();
        final Context lContext = lRequestHandler.getTestContext(lRequest);
        assertNotNull(lContext);
        assertEquals("en", lContext.getLanguage());
        assertTrue(lContext.getParameterNames2().isEmpty());
    }

}
