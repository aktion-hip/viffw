package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.impl.DBAdapterType;
import org.hip.kernel.dbaccess.DBAccessConfiguration;
import org.hip.kernel.dbaccess.DataSourceRegistry;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** <p>
 * Utility class for testing purpose. Creating and deleting of entries in viftest.tblTest.
 * </p>
 * <p>
 * <b>Note:</b> the MySQL pool size has to be set to 200 (or greater)
 * </p>
 *
 * @author: Benno Luthiger */
public enum DataHouseKeeper {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(DataHouseKeeper.class);
    private static final int SLEEP_PERIOD = 100; // milliseconds

    // constants
    private static final String PROPERTIES_FILE = "vif_db.properties";

    private final static String SIMPLE_HOME_NAME = "org.hip.kernel.bom.impl.test.Test2DomainObjectHomeImpl";
    private final static String ALTERNATIVE_HOME = "org.hip.kernel.bom.impl.test.TestAlternativeHomeImpl";
    private final static String LINK_HOME_NAME = "org.hip.kernel.bom.impl.test.LinkGroupMemberHomeImpl";
    private final static String JOIN_HOME_NAME = "org.hip.kernel.bom.impl.test.Test2JoinedDomainObjectHomeImpl";
    private final static String GROUP_HOME_NAME = "org.hip.kernel.bom.impl.test.TestGroupHomeImpl";
    private final static String PARTICIPANT_HOME_NAME = "org.hip.kernel.bom.impl.test.TestParticipantHomeImpl";
    private final static String NESTED_HOME_NAME = "org.hip.kernel.bom.impl.test.TestNestedDomainObjectHomeImpl";

    // instance variables
    private Test2DomainObjectHomeImpl simpleHome = null;
    private LinkGroupMemberHomeImpl linkHome = null;
    private Test2JoinedDomainObjectHomeImpl joinHome = null;

    /** DataHouseKeeper default constructor. */
    DataHouseKeeper() {
    }

    private void guard() {
        if (!DataSourceRegistry.INSTANCE.isInitialized()) {
            DataSourceRegistry.INSTANCE.setFactory(new TestDataSourceFactory());
            try (InputStream stream = DataHouseKeeper.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                final Properties properties = new Properties();
                properties.load(stream);
                final DBAccessConfiguration config = new DBAccessConfiguration(
                        properties.getProperty("org.hip.vif.db.driver"),
                        properties.getProperty("org.hip.vif.db.server"),
                        properties.getProperty("org.hip.vif.db.schema"),
                        properties.getProperty("org.hip.vif.db.userId"),
                        properties.getProperty("org.hip.vif.db.password"));
                DataSourceRegistry.INSTANCE.setActiveConfiguration(config);
                return;
            } catch (final IOException exc) {
                LOG.error("Could not create the DBAccessConfiguration!", exc);
            }
            throw new RuntimeException("Unable to initialize DataHouseKeeper!");
        }
    }

    public Long createTestEntry(final String name) throws SQLException {
        guard();
        return createTestEntry(name, "Adam");
    }

    public Long createTestEntry(final String name, final String firstName) throws SQLException {
        guard();
        Long outID = null;
        try {
            final DomainObject newEntry = getSimpleHome().create();
            newEntry.set("Name", name);
            newEntry.set("Firstname", firstName);
            newEntry.set("Mail", "dummy1@aktion-hip.ch");
            newEntry.set("Sex", Long.valueOf(1));
            newEntry.set("Amount", Float.valueOf(12.45f));
            newEntry.set("Double", Float.valueOf(13.11f));
            outID = newEntry.insert(true);
            newEntry.release();
        } catch (final VException exc) {
            fail(exc.getMessage());
        }
        return outID;
    }

    public void createTestLinkEntry(final int inMebmerID, final int inGroupID) throws SQLException, VException {
        guard();
        final DomainObject lNew = getLinkHome().create();
        lNew.set("MemberID", Long.valueOf(inMebmerID));
        lNew.set("GroupID", Long.valueOf(inGroupID));
        lNew.insert();
        lNew.release();
    }

    public void deleteAllFromLink() throws SQLException, VException, InterruptedException {
        guard();
        deleteAllFrom("tblGroupAdmin");
    }

    public void deleteAllFromSimple() throws SQLException, VException, InterruptedException {
        guard();
        deleteAllFrom("tblTest");
    }

    public void deleteAllFromGroup() throws SQLException, VException, InterruptedException {
        guard();
        deleteAllFrom("tblGroup");
    }

    public void deleteAllFromParticipant() throws SQLException, VException, InterruptedException {
        guard();
        deleteAllFrom("tblParticipant");
    }

    @SuppressWarnings("static-access")
    void deleteAllFrom(final String inTableName) throws SQLException, VException, InterruptedException {
        guard();
        final Connection lConnection = DataSourceRegistry.INSTANCE.getConnection();
        final Statement lStatement = lConnection.createStatement();
        lStatement.execute("DELETE FROM " + inTableName);
        // lConnection.commit();
        lStatement.close();
        lConnection.close();
        Thread.currentThread();
        Thread.sleep(SLEEP_PERIOD);
    }

    public ResultSet executeQuery(final String inSQL) throws SQLException, VException {
        guard();
        final Connection lConnection = DataSourceRegistry.INSTANCE.getConnection();
        final Statement lStatement = lConnection.createStatement();
        final ResultSet lResultSet = lStatement.executeQuery(inSQL);
        // lStatement.close();
        lConnection.close();
        return lResultSet;
    }

    public LinkGroupMemberHomeImpl getLinkHome() {
        guard();
        if (this.linkHome == null) {
            this.linkHome = (LinkGroupMemberHomeImpl) VSys.homeManager.getHome(LINK_HOME_NAME);
        }

        return this.linkHome;
    }

    // public int getNumberOfConnections() throws SQLException {
    // guard();
    // return PersistencyManager.singleton.getDefaultConnectionDriver().getNumberOfConnections();
    // }

    public Test2DomainObjectHomeImpl getSimpleHome() {
        guard();
        if (this.simpleHome == null) {
            this.simpleHome = (Test2DomainObjectHomeImpl) VSys.homeManager.getHome(SIMPLE_HOME_NAME);
        }

        return this.simpleHome;
    }

    public TestAlternativeHomeImpl getAlternativeHome() {
        guard();
        return (TestAlternativeHomeImpl) VSys.homeManager.getHome(ALTERNATIVE_HOME);
    }

    public Test2JoinedDomainObjectHomeImpl getJoinHome() {
        guard();
        if (this.joinHome == null) {
            this.joinHome = (Test2JoinedDomainObjectHomeImpl) VSys.homeManager.getHome(JOIN_HOME_NAME);
        }
        return this.joinHome;
    }

    public TestGroupHomeImpl getGroupHome() {
        guard();
        return (TestGroupHomeImpl) VSys.homeManager.getHome(GROUP_HOME_NAME);
    }

    public TestParticipantHomeImpl getParticipantHome() {
        guard();
        return (TestParticipantHomeImpl) VSys.homeManager.getHome(PARTICIPANT_HOME_NAME);
    }

    public TestNestedDomainObjectHomeImpl getNestedDomainObjectHomeImpl() {
        guard();
        return (TestNestedDomainObjectHomeImpl) VSys.homeManager.getHome(NESTED_HOME_NAME);
    }

    public Statement getStatement() throws SQLException, VException {
        guard();
        final Connection lConnection = DataSourceRegistry.INSTANCE.getConnection();
        return lConnection.createStatement();
    }

    public void insertTestEntry(final DomainObject inDomainObject) throws SQLException, VException {
        guard();
        inDomainObject.insert(true);
    }

    public boolean isDBMySQL() {
        guard();
        return DataSourceRegistry.INSTANCE.getAdapterType().isOfType(DBAdapterType.DB_TYPE_MYSQL.getType());
    }

    public boolean isDBOracle() {
        guard();
        return DataSourceRegistry.INSTANCE.getAdapterType().isOfType(DBAdapterType.DB_TYPE_ORACLE.getType());
    }

    public boolean isDBPostgreSQL() {
        guard();
        return DataSourceRegistry.INSTANCE.getAdapterType().isOfType(DBAdapterType.DB_TYPE_POSTGRESQL.getType());
    }

}
