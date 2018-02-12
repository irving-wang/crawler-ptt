/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt.model;

import java.util.Date;

/**
 * @author Irving
 *
 */
public class Push {
    
    private long id;
    private String post_id;
    private String board;
    private String tag;
    private long parent;
    private String userid;
    private String content;
    private Date item_date;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPost_id() {
        return post_id;
    }
    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public String getBoard()
	{
		return board;
	}
	public void setBoard(String board)
	{
		this.board = board;
	}
	public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public long getParent() {
        return parent;
    }
    public void setParent(long parent) {
        this.parent = parent;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getItem_date() {
        return item_date;
    }
    public void setItem_date(Date item_date) {
        this.item_date = item_date;
    }
    
/*
 * DROP TABLE IF EXISTS `ptt`.`stock_pushs`;
CREATE TABLE  `ptt`.`stock_pushs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` varchar(255) NOT NULL,
  `tag` smallint(5) unsigned NOT NULL,
  `parent` int(10) unsigned NOT NULL,
  `userid` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `item_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
}
