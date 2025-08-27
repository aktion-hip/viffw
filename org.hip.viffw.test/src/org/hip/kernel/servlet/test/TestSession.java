package org.hip.kernel.servlet.test;

import java.util.Hashtable;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

/**
 * Implementation of the HttpSession for testing purpose.
 *
 * @author: Benno Luthiger
 */
public class TestSession implements HttpSession {
    @SuppressWarnings("rawtypes")
    private Hashtable lValues = null;
    @SuppressWarnings("rawtypes")
    private Hashtable lAttributes = null;

    private final boolean isNewSession;

    /**
     * TestSession default constructor.
     */
    @SuppressWarnings("rawtypes")
    public TestSession() {
        super();
        this.lValues = new Hashtable(7);
        this.lAttributes = new Hashtable(7);
        this.isNewSession = true;
    }

    @Override
    public Object getAttribute(final String inKey) {
        return this.lAttributes.get(inKey);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public java.util.Enumeration getAttributeNames() {
        return this.lAttributes.keys();
    }

    /**
     * getCreationTime method comment.
     */
    @Override
    public long getCreationTime() {
        return 0;
    }

    /**
     * getId method comment.
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * getLastAccessedTime method comment.
     */
    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    /**
     * getMaxInactiveInterval method comment.
     */
    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }
    public Object getValue(final String inKey) {
        return this.lValues.get(inKey);
    }
    /**
     * getValueNames method comment.
     */
    public java.lang.String[] getValueNames() {
        return null;
    }
    /**
     * invalidate method comment.
     */
    @Override
    public void invalidate() {}
    @Override
    public boolean isNew() {
        return this.isNewSession;
    }
    @SuppressWarnings("unchecked")
    public void putValue(final String inKey, final Object inValue) {
        this.lValues.put(inKey, inValue);
    }
    /**
     * removeAttribute method comment.
     */
    @Override
    public void removeAttribute(final String arg1) {}
    /**
     * removeValue method comment.
     */
    public void removeValue(final String arg1) {}
    @Override
    @SuppressWarnings("unchecked")
    public void setAttribute(final String inKey, final Object inValue) {
        this.lAttributes.put(inKey, inValue);
    }
    /**
     * setMaxInactiveInterval method comment.
     */
    @Override
    public void setMaxInactiveInterval(final int arg1) {}
    @Override
    public ServletContext getServletContext() {
        return null;
    }
}
