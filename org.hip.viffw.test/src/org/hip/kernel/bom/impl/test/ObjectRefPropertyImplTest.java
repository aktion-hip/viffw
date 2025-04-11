package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.ObjectRefProperty;
import org.hip.kernel.bom.PropertySet;
import org.hip.kernel.bom.impl.ObjectRefPropertyImpl;
import org.hip.kernel.bom.impl.PropertySetImpl;
import org.hip.kernel.sys.VSys;

/**
 * @author: Benno Luthiger
 */
public class ObjectRefPropertyImplTest {

    public void testGetValue() {
        try {
            final DomainObject lDomainObject = ((TestRelDefDomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.TestRelDefDomainObjectHomeImpl")).create();
            lDomainObject.set("TestID", new Integer(20));
            final PropertySet lPropertySet = new PropertySetImpl(lDomainObject);
            assertNotNull(lPropertySet);

            final ObjectRefProperty lObjectRef = new ObjectRefPropertyImpl(lPropertySet, "FirstName");
            final Object lRet = lObjectRef.getValue();
            assertNotNull(lRet);
        }
        catch (final org.hip.kernel.bom.SettingException exc) {
            fail(exc.getMessage());
        }
        catch (final org.hip.kernel.bom.BOMException exc) {
            fail(exc.getMessage());
        }
    }
}
