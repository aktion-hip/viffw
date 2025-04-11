package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import org.hip.kernel.bom.BOMException;
import org.hip.kernel.bom.BOMNotFoundException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** @author: Benno Luthiger */
public class DomainObjectImplTest {
    private final PrivateDomainObjectHome lPrivatHome = new PrivateDomainObjectHome();

    @SuppressWarnings("serial")
    public class PrivateDomainObjectHome extends Test2DomainObjectHomeImpl {
        public PrivateDomainObjectHome() {
            super();
        }

        public int getNumberOfReleased() {
            return releasedObjects().size();
        }

        @Override
        public String getObjectClassName() {
            return "org.hip.kernel.bom.impl.test.DomainObjectImplTest$PrivateDomainObject";
        }

        @Override
        public DomainObject create() throws BOMException {
            final PrivateDomainObject outBOM = (PrivateDomainObject) newInstance();
            outBOM.initForNew();
            return outBOM;
        }

        @Override
        public DomainObject newInstance() throws BOMException {
            DomainObject outBOM;
            final Iterator<GeneralDomainObject> iter = releasedObjects().iterator();
            if (iter.hasNext()) {
                outBOM = (DomainObject) iter.next();
                releasedObjects().remove(outBOM);
            }
            else {
                outBOM = new PrivateDomainObject();
            }
            return outBOM;
        }
    }

    @SuppressWarnings("serial")
    private class PrivateDomainObject extends Test2DomainObjectImpl {
        private final static String lSuperHome = "org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl";

        @Override
        public String getHomeClassName() {
            return lSuperHome;
        }

        protected void initForNew() {
            initializeForNew();
        }

        @Override
        public void release() {
            DomainObjectImplTest.this.lPrivatHome.release(this);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDelete() throws VException, SQLException {
        assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
        final String lName = "Dummy";
        final String lFirstname = "Fi";

        // create a new domain objects and insert it
        final DomainObject lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(1, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

        // test find
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname);

        // update
        DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
        lFound.delete(true);

        // test find deleted
        try {
            lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            fail("Should not find deleted");
        } catch (final org.hip.kernel.bom.BOMNotFoundException exc) {
            // intentionally left empty
        }
        assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
    }

    @Test
    public void testInsert() throws VException, SQLException {
        final int lCount = DataHouseKeeper.INSTANCE.getSimpleHome().getCount();
        assertEquals(0, lCount);

        final String lName = "Dummy";
        final String lFirstname = "Fi";

        // create two new domain objects and insert them
        DomainObject lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        Long lObjectID = lDomainObject.insert(true);
        assertEquals(lObjectID.toString(), lDomainObject.get(Test2DomainObjectHomeImpl.KEY_ID).toString());
        lDomainObject.release();

        lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname + lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        lObjectID = lDomainObject.insert(true);
        assertEquals(lObjectID.toString(), lDomainObject.get(Test2DomainObjectHomeImpl.KEY_ID).toString());
        lDomainObject.release();

        assertEquals(2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

        // test find
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname);

        DomainObject lFound1 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        final DomainObject lFound2 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertTrue(lFound1.equals(lFound2));
        assertEquals(lFound1.hashCode(), lFound2.hashCode());

        // domain object should be equal even if their content differs
        // as long as their keys are equal
        lFound1.set("Firstname", lFirstname + lFirstname);
        assertTrue(lFound1.equals(lFound2));
        assertEquals(lFound1.hashCode(), lFound2.hashCode());

        lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        assertTrue(!lFound1.equals(lDomainObject));
        assertTrue(lFound1.hashCode() != lDomainObject.hashCode());

        lDomainObject.release();
        lFound1.release();
        lFound2.release();

        // test storing of primitive data types
        lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lDomainObject.set(Test2DomainObjectHomeImpl.KEY_NAME, lName);
        lDomainObject.set(Test2DomainObjectHomeImpl.KEY_FIRSTNAME, lFirstname);
        lDomainObject.set(Test2DomainObjectHomeImpl.KEY_SEX, 1); // primitive data type
        lObjectID = lDomainObject.insert(true);

        lKey = new KeyObjectImpl();
        lKey.setValue(Test2DomainObjectHomeImpl.KEY_ID, lObjectID);
        lFound1 = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        final Object lValue = lFound1.get(Test2DomainObjectHomeImpl.KEY_SEX);
        assertEquals(new BigDecimal(1), lValue);
    }

    @Test
    public void testRelease() throws VException, SQLException {
        assertEquals(0, this.lPrivatHome.getCount());
        final int lNumberOfReleased = this.lPrivatHome.getNumberOfReleased();

        final String lName = "Dummy";
        final String lFirstname = "Fi";

        // create two new domain objects and insert them
        DomainObject lDomainObject = this.lPrivatHome.create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(1, this.lPrivatHome.getCount());

        // release first
        lDomainObject.release();
        assertEquals(lNumberOfReleased + 1, this.lPrivatHome.getNumberOfReleased());

        // create second with released instance
        lDomainObject = this.lPrivatHome.create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname + lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);
        assertEquals(2, this.lPrivatHome.getCount());

        // release first instance again
        lDomainObject.release();
        assertEquals(lNumberOfReleased + 1, this.lPrivatHome.getNumberOfReleased());

        // test find
        final KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname);

        // get 3 instances
        final DomainObject lFound1 = this.lPrivatHome.findByKey(lKey);
        final DomainObject lFound2 = this.lPrivatHome.findByKey(lKey);
        lDomainObject = this.lPrivatHome.create();

        // release all instances
        lDomainObject.release();
        lFound1.release();
        lFound2.release();
        assertEquals(lNumberOfReleased + 3, this.lPrivatHome.getNumberOfReleased());
    }

    @Test
    public void testUpdate() throws VException, SQLException {
        final String lName = "Dummy";
        final String lFirstname = "Fi";

        // create a new domain objects and insert it
        final DomainObject lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
        lDomainObject.set("Name", lName);
        lDomainObject.set("Firstname", lFirstname);
        lDomainObject.set("Sex", new Integer(1));
        lDomainObject.insert(true);

        // test find
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname);

        // update
        DomainObject lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        lFound.set("Firstname", lFirstname + lFirstname);
        lFound.update(true);

        // test find updated
        try {
            lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
            fail("Should not find object with Firstname=" + lFirstname);
        } catch (final BOMNotFoundException exc) {
            assertNotNull(exc);
        }

        lKey = new KeyObjectImpl();
        lKey.setValue("Name", lName);
        lKey.setValue("Firstname", lFirstname + lFirstname);
        lFound = DataHouseKeeper.INSTANCE.getSimpleHome().findByKey(lKey);
        assertNotNull(lFound);
    }

}
