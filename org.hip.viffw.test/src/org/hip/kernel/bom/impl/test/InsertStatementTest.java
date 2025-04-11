package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.hip.kernel.bom.impl.InsertStatement;
import org.hip.kernel.exc.VException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * @author: Benno Luthiger
 */
public class InsertStatementTest {

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testCommit() {
        try {
            assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

            final String lSQL1 = "INSERT INTO TBLTEST( FDOUBLE, SFIRSTNAME, SLANGUAGE, TESTID, SFAX, SMAIL, STEL, SSTREET, SPLZ, SPASSWORD, FAMOUNT, SCITY, DTMUTATION, BSEX, SNAME )VALUES (NULL, 'Fi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'Dummy' )";
            final String lSQL2 = "INSERT INTO TBLTEST( FDOUBLE, SFIRSTNAME, SLANGUAGE, TESTID, SFAX, SMAIL, STEL, SSTREET, SPLZ, SPASSWORD, FAMOUNT, SCITY, DTMUTATION, BSEX, SNAME )VALUES (NULL, 'Adam', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'Pfiff' )";

            final InsertStatement lStatement = new InsertStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
            final Vector<String> lInserts = new Vector<String>();
            lInserts.addElement(lSQL1);
            lInserts.addElement(lSQL2);
            lStatement.setInserts(lInserts);

            final Collection<Long> lAutoKeys = lStatement.executeInsert();
            lStatement.commit();
            assertEquals(2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
            assertEquals(2, countAutoKeys(lAutoKeys));
        }
        catch (final SQLException exc) {
            fail(exc.getMessage());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testDo() {
        try {
            assertEquals(0, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());

            final String lSQL1 = "INSERT INTO TBLTEST( FDOUBLE, SFIRSTNAME, SLANGUAGE, TESTID, SFAX, SMAIL, STEL, SSTREET, SPLZ, SPASSWORD, FAMOUNT, SCITY, DTMUTATION, BSEX, SNAME )VALUES (NULL, 'Fi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'Dummy' )";
            final String lSQL2 = "INSERT INTO TBLTEST( FDOUBLE, SFIRSTNAME, SLANGUAGE, TESTID, SFAX, SMAIL, STEL, SSTREET, SPLZ, SPASSWORD, FAMOUNT, SCITY, DTMUTATION, BSEX, SNAME )VALUES (NULL, 'Adam', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'Pfiff' )";

            final InsertStatement lStatement = new InsertStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
            final Vector<String> lInserts = new Vector<String>();
            lInserts.addElement(lSQL1);
            lInserts.addElement(lSQL2);
            lStatement.setInserts(lInserts);

            final Collection<Long> lAutoKeys = lStatement.executeInsert();
            lStatement.close();
            assertEquals(2, DataHouseKeeper.INSTANCE.getSimpleHome().getCount());
            assertEquals(2, countAutoKeys(lAutoKeys));

            //mySQL can't rollback
            try {
                lStatement.rollback();
            }
            catch (final Error err) {
                lStatement.close();
            }
        }
        catch (final SQLException exc) {
            fail(exc.getMessage());
        }
        catch (final VException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testException() {
        InsertStatement lStatement = null;
        try {
            lStatement = new InsertStatement(DataHouseKeeper.INSTANCE.getSimpleHome());
            final Vector<String> lInserts = new Vector<String>();
            lInserts.addElement("Insert quatsch");
            lStatement.setInserts(lInserts);
            lStatement.executeInsert();
            fail("Shouldn't get here!");
        }
        catch (final SQLException exc) {
        }
        finally {
            if (lStatement != null) {
                lStatement.close();
            }
        }
    }

    private int countAutoKeys(final Collection<Long> inKeys) {
        int outNumber = 0;
        for (final Long lKey : inKeys) {
            System.out.println(lKey.toString());
            outNumber++;
        }
        return outNumber;
    }
}
