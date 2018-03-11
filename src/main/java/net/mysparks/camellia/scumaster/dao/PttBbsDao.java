/**
 * 
 */
package net.mysparks.camellia.scumaster.dao;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import net.mysparks.camellia.scumaster.db.Sql2oPersistence;
import net.mysparks.camellia.scumaster.model.Article;
import net.mysparks.camellia.scumaster.model.Push;

/**
 * @author Irving
 *
 */
public class PttBbsDao extends Sql2oPersistence{
    
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public int insertArticle(Article bean) {
	String insertSql = "insert into articles(uri, board, userid, nickname, title, item_date, content, ip, parent_uri, pushs) "
		+ "values (:uri, :board, :userid, :nickname, :title, :item_date, :content, :ip, :parent_uri, :pushs)";
	int c = 0;
	try (Connection con = beginTransaction()) {
	    con.createQuery(insertSql).addParameter("uri", bean.getUri())
	    	.addParameter("board", bean.getBoard())
		    .addParameter("userid", bean.getUserid())
		    .addParameter("nickname", bean.getNickname())
		    .addParameter("title", bean.getTitle())
		    .addParameter("item_date", bean.getItem_date())
		    .addParameter("content", bean.getContent())
		    .addParameter("ip", bean.getIp())
		    .addParameter("parent_uri", bean.getParent_uri())
		    .addParameter("pushs", bean.getPushs())
		    .executeUpdate();
	    c++;
	    for (Push push : bean.getPushList()) {
		    c += insertPush(push, con);
	    }
	    con.commit();
	}catch(Sql2oException e){
	    log.error("PttBbsDao insertArticle commit fail: {}", e.getMessage());
	}
	return c;
    }

    public int insertPush(Push bean, Connection con) {
	String insertSql = "insert into pushs(id, post_id, board, tag, userid, content, item_date) " + "values (:id, :post_id, :board, :tag, :userid, :content, :item_date)";

	con.createQuery(insertSql).addParameter("id", bean.getId())
	    	.addParameter("post_id", bean.getPost_id())
	    	.addParameter("board", bean.getBoard())
	    	.addParameter("tag", bean.getTag())
	    	.addParameter("userid", bean.getUserid())
	    	.addParameter("content", bean.getContent())
	    	.addParameter("item_date", bean.getItem_date())
	    	.executeUpdate();

	return 1;
    }

}
