package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.hip.kernel.bom.AlternativeModel;
import org.hip.kernel.bom.AlternativeModelFactory;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.sys.VObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
 * Created on Sep 22, 2004
 */
public class AlternativeModellTest {
    private final String[] expected = {"AAAAAAAAAA", "BBBBBBBB", "CCCCC"};

    private class TestAlternativeModel extends VObject implements AlternativeModel {
        public String name = "";
        public String firstName = "";
        @Override
        public String toString() {
            return this.name + ":" + this.firstName;
        }
    }

    private class TestAlternativeFactory extends VObject implements AlternativeModelFactory {
        public TestAlternativeFactory() {
            super();
        }
        @Override
        public AlternativeModel createModel(final ResultSet inResultSet) throws SQLException {
            final TestAlternativeModel outModel = new TestAlternativeModel();
            outModel.name = inResultSet.getString("sName");
            outModel.firstName = inResultSet.getString("sFirstname");
            return outModel;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        populate();
    }

    @AfterEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    public void testDo() throws Exception {
        final TestAlternativeHomeImpl lHome = DataHouseKeeper.INSTANCE.getAlternativeHome();
        final QueryResult lResult = lHome.select();
        final Collection<AlternativeModel> lSet = lResult.load(new TestAlternativeFactory());

        assertEquals(this.expected.length, lSet.size());

        String lCheck = "";
        for (final AlternativeModel lModel : lSet) {
            lCheck += lModel + ";";
        }
        for (int i = 0; i < this.expected.length; i++) {
            assertTrue(lCheck.indexOf(this.expected[i] + ":" + this.expected[i].toLowerCase()) >= 0);
        }
    }

    private void populate() throws Exception {
        DomainObject lDomainObject;
        for (int i = 0; i < this.expected.length; i++) {
            lDomainObject = DataHouseKeeper.INSTANCE.getSimpleHome().create();
            lDomainObject.set("Name", this.expected[i]);
            lDomainObject.set("Firstname", this.expected[i].toLowerCase());
            lDomainObject.set("Sex", Integer.valueOf(1));
            lDomainObject.insert(true);
        }
    }
}
