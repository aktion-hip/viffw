package org.hip.kernel.servlet.test;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.impl.AbstractTask;

/**
 * Concrete implementation of a Task for testing purpose.
 *
 * @author: Benno Luthiger
 */
public class TestTask extends AbstractTask {
    /**
     * TestTask default constructor.
     */
    public TestTask() {
        super();
    }
    /**
     * Runs this task
     */
    @Override
    public void run() throws org.hip.kernel.exc.VException {
        final Context lContext = getContext();
        final TestHtmlPage lPage = new TestHtmlPage(lContext);
        final TestXSLView lView = new TestXSLView(lContext);
        lPage.add(lView);
        lContext.setView(lPage);
    }
}
