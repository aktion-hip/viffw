package org.hip.kernel.servlet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import org.hip.kernel.servlet.impl.ServletContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 * Created on 17.02.2006
 */
public class ServletContainerTest {
    private final static String SERVLET_INFO = "Apache Tomcat Test";
    private final static String SERVLET_BASE = "Tomcat/Instance/Base/Path";
    private final static String SERVLET_HOME = "Tomcat/App/Home/Path";

    @BeforeEach
    public void setUp() throws Exception {
        //we assume Tomcat, therefore set "catalina.base" and "catalina.home"
        ServletContainer.getInstance().setServerInfo(SERVLET_INFO);
        final Properties lProperties = System.getProperties();
        lProperties.setProperty("catalina.base", SERVLET_BASE);
        lProperties.setProperty("catalina.home", SERVLET_HOME);
    }

    @Test
    public void testInfo() {
        assertEquals("Server Info", SERVLET_INFO, ServletContainer.getInstance().getServerInfo());
        assertEquals("Servlet Base", SERVLET_BASE, ServletContainer.getInstance().getBasePath());
        assertEquals("Servlet Home", SERVLET_HOME, ServletContainer.getInstance().getHomePath());
    }

}
