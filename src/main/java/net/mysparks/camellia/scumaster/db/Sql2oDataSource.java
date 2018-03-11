/**
 * 
 */
package net.mysparks.camellia.scumaster.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sql2o.Sql2o;

import net.mysparks.camellia.scumaster.CrawlerConfiguration;

/**
 * @author Irving
 *
 */
public class Sql2oDataSource {
    
    public final static Sql2oDataSource INSTANCE = new Sql2oDataSource();
    private DataSource dataSource;
    private Sql2o sql2o;
    
    private Sql2oDataSource(){
	init();
	sql2o = new Sql2o(getDataSource());
    }
    
    protected void init() {
	this.dataSource = setupDataSource(CrawlerConfiguration.DATASOURCE_URL,
		CrawlerConfiguration.DATASOURCE_DRIVECLASS, CrawlerConfiguration.DATASOURCE_USER,
		CrawlerConfiguration.DATASOURCE_PASSWORD);
    }
    
    public Sql2o getSql2o() {
	return sql2o;
    }
    
//    public Connection getConnection() {
//	return sql2o.beginTransaction().getJdbcConnection();
//    }
//    
//    public org.sql2o.Connection beginTransaction() {
//	return sql2o.beginTransaction();
//    }
//    
//    public org.sql2o.Connection beginTransaction(int isolationLevel){
//	return sql2o.beginTransaction(isolationLevel);
//    }
    
    protected DataSource getDataSource() {
        return dataSource;
    }
    
    protected DataSource setupDataSource(String connectURI, String driverClass, String user, String password) {
	BasicDataSource ds = new BasicDataSource();
	ds.setDriverClassName(driverClass);
	ds.setUrl(connectURI);
	ds.setUsername(user);
	ds.setPassword(password);
	return ds;
    }
}
