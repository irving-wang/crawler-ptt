/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt.db;

import org.sql2o.Connection;

import com.asuscloud.mymaster.crawler.ptt.model.Article;
import com.asuscloud.mymaster.crawler.ptt.model.Push;

/**
 * @author Irving
 *
 */
public class PttBbsDao {

    public int insertArticle(Article bean) {
	String insertSql = "insert into stock_articles(uri, userid, nickname, title, item_date, content, ip, parent_uri, pushs) "
		+ "values (:uri, :userid, :nickname, :title, :item_date, :content, :ip, :parent_uri, :pushs)";

	try (Connection con = Persistence.getSql2o().open()) {
	    con.createQuery(insertSql).addParameter("uri", bean.getUri())
		    .addParameter("userid", bean.getUserid())
		    .addParameter("nickname", bean.getNickname())
		    .addParameter("title", bean.getTitle())
		    .addParameter("item_date", bean.getItem_date())
		    .addParameter("content", bean.getContent())
		    .addParameter("ip", bean.getIp())
		    .addParameter("parent_uri", bean.getParent_uri())
		    .addParameter("pushs", bean.getPushs())
		    .executeUpdate();
	}
	return 1;
    }

    public int insertPush(Push bean) {
	String insertSql = "insert into stock_pushs(id, post_id, tag, parent, userid, content, item_date) " + "values (:id, :post_id, :tag, :parent, :userid, :content, :item_date)";

	try (Connection con = Persistence.getSql2o().open()) {
	    con.createQuery(insertSql).addParameter("id", bean.getId())
	    		.addParameter("post_id", bean.getPost_id())
	    		.addParameter("tag", bean.getTag())
	    		.addParameter("parent", bean.getParent())
	    		.addParameter("userid", bean.getUserid())
	    		.addParameter("content", bean.getContent())
	    		.addParameter("item_date", bean.getItem_date())
		    .executeUpdate();
	}
	return 1;
    }

}
