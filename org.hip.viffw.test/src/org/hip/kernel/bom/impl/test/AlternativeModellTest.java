package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import org.hip.kernel.bom.AlternativeModel;
import org.hip.kernel.bom.AlternativeModelFactory;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.AlternativeQueryResult;
import org.hip.kernel.sys.VObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Benno Luthiger
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
    void setUp() throws Exception {
        populate();
    }

    @AfterEach
    void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFromSimple();
    }

    @Test
    void testDo() throws Exception {
        final TestAlternativeHomeImpl home = DataHouseKeeper.INSTANCE.getAlternativeHome();
        home.setFactory(new TestAlternativeFactory());
        final QueryResult result = home.select();

        Collection<AlternativeModel> results = Collections.emptyList();
        if (result instanceof final AlternativeQueryResult altResult) {
            results = altResult.getAlternativeModels();
        }

        assertEquals(this.expected.length, results.size());

        String lCheck = "";
        for (final AlternativeModel model : results) {
            lCheck += model + ";";
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
