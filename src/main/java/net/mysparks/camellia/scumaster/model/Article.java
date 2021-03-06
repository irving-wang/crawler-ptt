/**
 * 
 */
package net.mysparks.camellia.scumaster.model;

import java.util.Date;
import java.util.List;

/**
 * @author Irving
 *
 */
public class Article {
    
    private String uri;
    private String board;
    private String userid;
    private String nickname;
    private String title;
    private Date item_date;
    private String content;
    private String ip;
    private int pushs;
    private String parent_uri;
    
    private List<Push> pushList;
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getBoard()
	{
		return board;
	}
	public void setBoard(String board)
	{
		this.board = board;
	}
	public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getItem_date() {
        return item_date;
    }
    public void setItem_date(Date item_date) {
        this.item_date = item_date;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPushs() {
        return pushs;
    }
    public void setPushs(int pushs) {
        this.pushs = pushs;
    }
    public String getParent_uri() {
        return parent_uri;
    }
    public void setParent_uri(String parent_uri) {
        this.parent_uri = parent_uri;
    }
    public List<Push> getPushList() {
        return pushList;
    }
    public void setPushList(List<Push> pushList) {
        this.pushList = pushList;
    }
    
/*
 * DROP TABLE IF EXISTS `ptt`.`stock_articles`;
CREATE TABLE  `ptt`.`stock_articles` (
  `uri` varchar(255) NOT NULL,
  `userid` varchar(45) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `title` varchar(255) NOT NULL,
  `item_date` datetime NOT NULL,
  `content` text NOT NULL,
  `ip` varchar(45) NOT NULL,
  `pushs` smallint(5) unsigned NOT NULL,
  `parent_uri` varchar(255) NOT NULL,
  PRIMARY KEY (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
}
