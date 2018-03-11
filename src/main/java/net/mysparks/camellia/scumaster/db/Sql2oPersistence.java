/**
 * 
 */
package net.mysparks.camellia.scumaster.db;

import java.sql.Connection;

/**
 * @author Irving
 *
 */
public class Sql2oPersistence {
    
    private Sql2oDataSource datasource = Sql2oDataSource.INSTANCE;
    
    public Connection getConnection() {
	return datasource.getSql2o().beginTransaction().getJdbcConnection();
    }
    
    public org.sql2o.Connection beginTransaction() {
	return datasource.getSql2o().beginTransaction();
    }
    
    public org.sql2o.Connection beginTransaction(int isolationLevel){
	return datasource.getSql2o().beginTransaction(isolationLevel);
    }
}
