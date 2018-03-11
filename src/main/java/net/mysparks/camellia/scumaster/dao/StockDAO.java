/**
 * 
 */
package net.mysparks.camellia.scumaster.dao;

import org.apache.ibatis.session.SqlSession;

import net.mysparks.camellia.scumaster.db.MyBatisPersistence;
import net.mysparks.camellia.scumaster.model.Ohlc;
import net.mysparks.camellia.scumaster.model.Stock;

/**
 * @author Irving
 *
 */
public class StockDAO extends MyBatisPersistence {
    
    public int insertStock(Stock bean) {
	try (SqlSession session = openSession(true);) {
	    return session.insert("insertStock", bean);
	}
    }
    
    public int insertQuote(Ohlc bean) {
	try (SqlSession session = openSession(true);) {
	    return session.insert("insertOhlc", bean);
	}
    }
}
