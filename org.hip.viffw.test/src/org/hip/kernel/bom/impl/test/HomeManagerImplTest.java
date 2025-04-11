package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.hip.kernel.bom.HomeManager;
import org.hip.kernel.bom.impl.HomeManagerImpl;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class HomeManagerImplTest {

    @Test
    public void testGetHome() {
        final HomeManager lManager = HomeManagerImpl.getSingleton();
        assertNotNull(lManager);

        final String lHomeClassName = "org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl";
        assertNotNull(lManager.getHome(lHomeClassName));
        assertNull(lManager.getHome(lHomeClassName + "!!"));
    }
}
