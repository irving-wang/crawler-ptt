/**
 * 
 */
package net.mysparks.camellia.scumaster.dao;

import org.apache.ibatis.session.SqlSession;

import net.mysparks.camellia.scumaster.db.MyBatisPersistence;
import net.mysparks.camellia.scumaster.model.Article;
import net.mysparks.camellia.scumaster.model.Push;

/**
 * @author Irving
 *
 */
public class ArticleDao extends MyBatisPersistence {

    public int insertArticleAndPush(Article bean) {
	try (SqlSession session = openSession();) {
	    int c = session.insert("insertArticle", bean);
	    c++;
	    for (Push push : bean.getPushList()) {
		c += session.insert("insertPush", push);
	    }
	    session.commit(true);
	    return c;
	}
    }
}
