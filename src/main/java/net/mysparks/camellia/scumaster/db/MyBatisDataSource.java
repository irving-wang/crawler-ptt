/**
 * 
 */
package net.mysparks.camellia.scumaster.db;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mysparks.camellia.scumaster.CrawlerConfiguration;

/**
 * @author Irving
 *
 */
public class MyBatisDataSource {
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public final static MyBatisDataSource INSTANCE = new MyBatisDataSource();
    private SqlSessionFactory sqlSessionFactory;
    
    private MyBatisDataSource(){
	init();
    }
    
    private void init() {
//	try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");){
	try (InputStream inputStream = Resources.getUrlAsStream("file:///"+CrawlerConfiguration.MYBATIS_CONFIG);){
	    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	} catch (IOException e) {
	    log.error("MyBatis init SqlSessionFactory fail.", e);
	}
    }

    protected DataSource setupDataSource(String connectURI, String driverClass, String user, String password) {
	PooledDataSource ds = new PooledDataSource();
	ds.setDriver(driverClass);
	ds.setUrl(connectURI);
	ds.setUsername(user);
	ds.setPassword(password);
	return ds;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
    
    
}
