/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt.parser;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.asuscloud.mymaster.crawler.ptt.model.Article;
import com.asuscloud.mymaster.crawler.ptt.model.Push;

/**
 * @author Irving
 *
 */
public class PttArticleParser {

    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);

    public Article parseArticle(String html) {
	Article bean = new Article();

	try {
	    Document doc = Jsoup.parse(html);// Jsoup.connect("http://www.ptt.cc/bbs/Stock/").get();

	    // Elements metaline = doc.select("div .article-metaline");
	    Elements metaline = doc.select(".article-meta-value");
//	    doc.select("span.f2").forEach(System.out::println);
	    String author = metaline.get(0).text();
	    bean.setUserid(author.substring(0, author.indexOf("(")).trim());
	    bean.setNickname(author.substring(author.indexOf("(") + 1, author.lastIndexOf(")")).trim());

	    bean.setTitle(metaline.get(2).text());
	    bean.setItem_date(format.parse(metaline.get(3).text()));
	    bean.setContent(doc.select("#main-content").get(0).ownText());

	    String footer = doc.select("span.f2").get(0).text();
	    bean.setIp(footer.substring(footer.lastIndexOf(":") + 1, footer.length()).trim());
	    
	    bean.setPushList(new LinkedList<Push>());
	    Elements e = doc.select(".push");
	    e.forEach(o->{
		Push push = new Push();
		o.children().forEach(System.out::println);
		
		bean.getPushList().add(push);
	    });

	    // System.out.println(doc.select("#main-content").get(0).ownText());

	    // metaline.get(3).childNodes().forEach(System.out::println);

	    System.out.println(ToStringBuilder.reflectionToString(bean));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return bean;
    }

    public Push parsePush(String html) {
	Push result = new Push();

	return result;
    }
}
