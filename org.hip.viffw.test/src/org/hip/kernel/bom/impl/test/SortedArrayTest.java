package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class SortedArrayTest {

    @Test
    public void testAdd() throws Exception {
        final Test2DomainObjectHomeImpl lHome = (Test2DomainObjectHomeImpl)VSys.homeManager.getHome("org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl");

        final String lName = "Dummy";
        final String lFirstname = "Fi";
        final String lMail = "dummy1@aktion-hip.ch";

        //create domain object
        final DomainObject lNew = lHome.create();
        lNew.set("Name", lName);
        lNew.set("Firstname", lFirstname);
        lNew.set("Mail", lMail);
        lNew.set("Sex", new Integer(1));
        lNew.set("Amount", new Float(12.45));
        lNew.set("Double", new Float(13.11));

        final DomainObject lNew2 = lHome.create();
        lNew2.set("Name", "Beta");
        lNew2.set("Firstname", lFirstname);
        lNew2.set("Mail", lMail);
        lNew2.set("Sex", new Integer(1));
        lNew2.set("Amount", new Float(12.45));
        lNew2.set("Double", new Float(13.11));

        final DomainObject lNew3 = lHome.create();
        lNew3.set("Name", "Alpha");
        lNew3.set("Firstname", lFirstname);
        lNew3.set("Mail", lMail);
        lNew3.set("Sex", new Integer(1));
        lNew3.set("Amount", new Float(12.45));
        lNew3.set("Double", new Float(13.11));

        //create sorted array of domain objects
        TestSortedArray lSortedArray = new TestSortedArray();
        lSortedArray.add(lNew);
        lSortedArray.add(lNew2);
        lSortedArray.add(lNew3);

        assertEquals(3, lSortedArray.size());
        assertTrue(lNew3 == lSortedArray.elementAt(0));
        assertTrue(lNew2 == lSortedArray.elementAt(1));
        assertTrue(lNew == lSortedArray.elementAt(2));
        assertTrue(lNew != lSortedArray.elementAt(1));

        lSortedArray.remove(1);
        assertEquals(2, lSortedArray.size());
        assertTrue(lNew3 == lSortedArray.elementAt(0));
        assertTrue(lNew == lSortedArray.elementAt(1));

        //create sorted array of unique domain objects
        lSortedArray = new TestSortedArray();
        lSortedArray.addUnique(lNew);
        lSortedArray.addUnique(lNew2);
        lSortedArray.addUnique(lNew3);
        lSortedArray.addUnique(lNew2);

        assertEquals(3, lSortedArray.size());
        assertTrue(lNew3 == lSortedArray.elementAt(0));
        assertTrue(lNew2 == lSortedArray.elementAt(1));
        assertTrue(lNew == lSortedArray.elementAt(2));
        assertTrue(lNew != lSortedArray.elementAt(1));

        lSortedArray.remove(1);
        assertEquals(2, lSortedArray.size());
        assertTrue(lNew3 == lSortedArray.elementAt(0));
        assertTrue(lNew == lSortedArray.elementAt(1));
    }

}
