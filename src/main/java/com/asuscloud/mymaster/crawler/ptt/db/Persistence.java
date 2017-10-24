/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt.db;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sql2o.Sql2o;

import com.asuscloud.mymaster.crawler.ptt.CrawlerConfiguration;

/**
 * @author Irving
 *
 */
public class Persistence {

    static {
	init();
    }
    private static Sql2o sql2o;

    public static Sql2o getSql2o() {
	return sql2o;
    }

    public static void init() {
	DataSource datasource = setupDataSource(CrawlerConfiguration.DATASOURCE_URL,
		CrawlerConfiguration.DATASOURCE_DRIVECLASS, CrawlerConfiguration.DATASOURCE_USER,
		CrawlerConfiguration.DATASOURCE_PASSWORD);
	sql2o = new Sql2o(datasource);
    }

    public static DataSource setupDataSource(String connectURI, String driverClass, String user, String password) {
	BasicDataSource ds = new BasicDataSource();
	ds.setDriverClassName(driverClass);
	ds.setUrl(connectURI);
	ds.setUsername(user);
	ds.setPassword(password);
	return ds;
    }
}
