package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.RequestException;
import org.hip.kernel.servlet.TaskManager;
import org.hip.kernel.servlet.impl.AbstractRequestHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Concrete implementation of an AbstractRequestHandler for testing purpose.
 *
 * @author: Benno Luthiger
 */
@SuppressWarnings("serial")
public class TestRequestHandler extends AbstractRequestHandler {
    private static final String SYS_NAME = "vif";
    private static final String CONTEXT_CLASSNAME = "org.hip.kernel.servlet.test.TestContext";

    /**
     * Handles GET-Requests.
     *
     * @param inRequest javax.servlet.http.HttpServletRequest
     * @param inResponse javax.servlet.http.HttpServletResponse
     * @exception javax.servlet.ServletException
     * @exception java.io.IOException
     */
    @Override
    public void doGet(final HttpServletRequest inRequest, final HttpServletResponse inResponse) throws ServletException, IOException {
        try {
            super.doGet(inRequest, inResponse);
        }
        catch (final Throwable ex) {
            this.handle(getContext(inRequest), ex);
        }
    }

    /**
     * Subclasses, which implement this method, return the name of the specialiced application-context.
     *
     * @return java.lang.String
     */
    @Override
    protected String getContextClassName() {
        return CONTEXT_CLASSNAME;
    }

    /**
     * Subclasses which implement this methode, have to return an instance
     * of the applications specific TaskManager.
     *
     * @return an instance of the taskmanager
     * @see org.hip.kernel.servlet.TaskManager
     */
    @Override
    protected TaskManager getTaskManager() {
        return TestTaskManager.getInstance();
    }

    /**
     * Opens the method getContext() for testing purpose
     */
    public Context getTestContext(final HttpServletRequest inRequest) {
        return getContext(inRequest);
    }

    /**
     * @param inContext 	org.hip.kernel.servlet.Context
     * @param inThrowable 	java.lang.Throwable
     */
    @Override
    protected void handle(final Context inContext, final Throwable inThrowable) {

        //handling of errors
        fail(inThrowable.getMessage());
    }

    /**
     * service method comment.
     */
    @Override
    public void service(final jakarta.servlet.ServletRequest arg1, final jakarta.servlet.ServletResponse arg2)
            throws jakarta.servlet.ServletException, java.io.IOException {
    }

    /**
     * Returns the name of the application's properties file.
     *
     * @return java.lang.String
     */
    @Override
    protected String getSysName() {
        return SYS_NAME;
    }

    @Override
    protected String getDefaultRequestType() throws RequestException {
        return "testtask";
    }
}
