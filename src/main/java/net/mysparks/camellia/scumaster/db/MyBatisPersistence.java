/**
 * 
 */
package net.mysparks.camellia.scumaster.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * @author Irving
 *
 */
public class MyBatisPersistence {
    
//    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private MyBatisDataSource datasource = MyBatisDataSource.INSTANCE;
    
//    private SqlSessionFactory sqlSessionFactory;
    
//    private MyBatisPersistence(){
//	super();
//    }
    
    public SqlSession openSession(){
	return datasource.getSqlSessionFactory().openSession();
    }
    
    public SqlSession openSession(boolean autoCommit){
	return datasource.getSqlSessionFactory().openSession(autoCommit);
    }
    
    public SqlSession openSession(TransactionIsolationLevel level){
	return datasource.getSqlSessionFactory().openSession(level);
    }
    
    public Connection getConnection() throws SQLException{
	return datasource.getSqlSessionFactory().openSession().getConnection();
    }

//    protected void init() {
//	String resource = "mybatis-config.xml";
//	try (InputStream inputStream = Resources.getResourceAsStream(resource);){
//	    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//	} catch (IOException e) {
//	    log.error("MyBatis init SqlSessionFactory fail.", e);
//	}
//    }
//
//    protected DataSource setupDataSource(String connectURI, String driverClass, String user, String password) {
//	PooledDataSource ds = new PooledDataSource();
//	ds.setDriver(driverClass);
//	ds.setUrl(connectURI);
//	ds.setUsername(user);
//	ds.setPassword(password);
//	return ds;
//    }
}
