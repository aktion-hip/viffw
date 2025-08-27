# VIF Framework Test

For that the unit tests can be run, the following preconditions have to be fulfilled:

- An accessible MySQL or MariaDB database has to provide tables according to the SQL script *createTestTables.sql*.

- The access to the database, i.e. the properties 
  *org.hip.vif.db.driver*, *org.hip.vif.db.server*, *org.hip.vif.db.schema*, *org.hip.vif.db.userId*, 
  *org.hip.vif.db.password* in `/src/vif_db.properties` have to be properly specified. 
  See `/src/vif_db.properties.sample`.
  
- The properties file `/src/vif.properties`. You can copy the file `/src/vif.properties.sample` and adjust the values.

- The class libraries for *xalan* (xalan.jar), *xerces* (xercesImpl.jar) and the *MySQL-JDBC* (mysql-connector-java-3.0.17-ga-bin.jar) have to be on the classpath.