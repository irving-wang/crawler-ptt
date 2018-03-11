/**
 * 
 */
package net.mysparks.camellia.scumaster.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.mysparks.camellia.scumaster.CrawlerConfiguration;

/**
 * @author Irving
 *
 */
public abstract class Persistence {

    private DataSource dataSource;
    
    protected Persistence() {
	init();
    }

    protected void init() {
	this.dataSource = setupDataSource(CrawlerConfiguration.DATASOURCE_URL,
		CrawlerConfiguration.DATASOURCE_DRIVECLASS, CrawlerConfiguration.DATASOURCE_USER,
		CrawlerConfiguration.DATASOURCE_PASSWORD);
    }
    
    protected Connection getConnection() throws SQLException{
	return getDataSource().getConnection();
    }
    
    protected DataSource getDataSource() {
        return dataSource;
    }

    protected abstract DataSource setupDataSource(String connectURI, String driverClass, String user, String password);
}
