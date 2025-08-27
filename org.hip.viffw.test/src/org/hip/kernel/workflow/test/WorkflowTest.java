package org.hip.kernel.workflow.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hip.kernel.workflow.WorkflowAware;
import org.junit.jupiter.api.Test;

/**
 * Simple test case for testing the workflow package.
 *
 * Created on 03.07.2003
 * @author Benno Luthiger
 * (inspired by itools by Juan David Ib��ez Palomar <jdavid@itaapy.com>)
 */
public class WorkflowTest {

    @Test
    public void testDo() throws Exception {
        final WorkflowAware lDocument = new TestDocument();

        assertEquals(0, ((TestDocument) lDocument).getNumberOfEnters());
        assertEquals(0, ((TestDocument) lDocument).getNumberOfLeaves());
        assertEquals(0, ((TestDocument) lDocument).getNumberOfTransitions());

        //do workflow
        assertEquals(TestDocument.STATE_PRIVATE, lDocument.getStateName());
        System.out.println("State now (1): " + lDocument.getStateName());
        System.out.println("do first request transition...");
        lDocument.doTransition(TestDocument.TRANS_REQUEST, new Object[] {"First Request"});
        assertEquals(1, ((TestDocument) lDocument).getNumberOfEnters());
        assertEquals(1, ((TestDocument) lDocument).getNumberOfLeaves());
        assertEquals(1, ((TestDocument) lDocument).getNumberOfTransitions());

        assertEquals(TestDocument.STATE_PENDING, lDocument.getStateName());
        System.out.println("State now (2): " + lDocument.getStateName());
        System.out.println("do first reject transition...");
        lDocument.doTransition(TestDocument.TRANS_REJECT, new Object[] {"First Reject"});
        assertEquals(2, ((TestDocument) lDocument).getNumberOfEnters());
        assertEquals(2, ((TestDocument) lDocument).getNumberOfLeaves());
        assertEquals(2, ((TestDocument) lDocument).getNumberOfTransitions());

        assertEquals(TestDocument.STATE_PRIVATE, lDocument.getStateName());
        System.out.println("State now (3): " + lDocument.getStateName());
        System.out.println("do second request transition...");
        lDocument.doTransition(TestDocument.TRANS_REQUEST, new Object[] {"Second Request"});
        assertEquals(3, ((TestDocument) lDocument).getNumberOfEnters());
        assertEquals(3, ((TestDocument) lDocument).getNumberOfLeaves());
        assertEquals(3, ((TestDocument) lDocument).getNumberOfTransitions());

        assertEquals(TestDocument.STATE_PENDING, lDocument.getStateName());
        System.out.println("State now (4): " + lDocument.getStateName());
        System.out.println("do final accept transition...");
        lDocument.doTransition(TestDocument.TRANS_ACCEPT, new Object[] {"Final Accept"});
        assertEquals(4, ((TestDocument) lDocument).getNumberOfEnters());
        assertEquals(4, ((TestDocument) lDocument).getNumberOfLeaves());
        assertEquals(4, ((TestDocument) lDocument).getNumberOfTransitions());

        assertEquals(TestDocument.STATE_PUBLIC, lDocument.getStateName());
        System.out.println("State now (5): " + lDocument.getStateName());
    }

    @Test
    public void testReBind() throws Exception {
        final TestDocument lDocument = new TestDocument();
        lDocument.reBind(TestDocument.STATE_PUBLIC);
        assertEquals(TestDocument.STATE_PUBLIC, lDocument.getStateName());
    }
}
