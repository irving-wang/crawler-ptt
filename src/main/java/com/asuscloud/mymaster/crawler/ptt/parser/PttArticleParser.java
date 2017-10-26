/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.asuscloud.mymaster.crawler.ptt.model.Article;
import com.asuscloud.mymaster.crawler.ptt.model.Push;

/**
 * @author Irving
 *
 */
public class PttArticleParser {

    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);

    public void parseArticle(String html, Article bean) {
	// Article bean = new Article();
	try {
	    Document doc = Jsoup.parse(html);// Jsoup.connect("http://www.ptt.cc/bbs/Stock/").get();

	    // Elements metaline = doc.select("div .article-metaline");
	    Elements metaline = doc.select(".article-meta-value");
	    // doc.select("span.f2").forEach(System.out::println);
	    String author = metaline.get(0).text();
	    bean.setUserid(author.substring(0, author.indexOf("(")).trim());
	    bean.setNickname(author.substring(author.indexOf("(") + 1, author.lastIndexOf(")")).trim());

	    bean.setTitle(metaline.get(2).text());
	    bean.setItem_date(format.parse(metaline.get(3).text()));
	    Element mainContent = doc.select("#main-content").get(0);
	    bean.setContent(mainContent.ownText());

	    String footer = mainContent.select("span.f2").get(0).text();
	    bean.setIp(footer.substring(footer.lastIndexOf(":") + 1, footer.length()).trim());

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(bean.getItem_date());
	    SimpleDateFormat pushFormat = new SimpleDateFormat("MM/dd HH:mm yyyy", Locale.US);

	    bean.setPushs(0);
	    bean.setPushList(new LinkedList<Push>());
	    Elements e = mainContent.select(".push");
	    e.forEach(o -> {
		o.children().forEach(System.out::println);
		bean.setPushs(bean.getPushs() + 1);
		Push push = new Push();
		push.setId(bean.getPushs());
		push.setBoard(bean.getBoard());
		push.setTag(o.select(".push-tag").text().trim());
		push.setUserid(o.select(".push-userid").text().trim());
		push.setContent(o.select(".push-content").text().substring(1).trim());
		try {
		    push.setItem_date(pushFormat
			    .parse(o.select(".push-ipdatetime").text().trim() + " " + cal.get(Calendar.YEAR)));
		} catch (ParseException e1) {
		    e1.printStackTrace();
		}
		push.setPost_id(bean.getUri());
		if (push.getTag().equals("→")) {
		    Push lastPush = bean.getPushList().get(bean.getPushList().size() - 1);
		    if (lastPush.getParent() > 0)
			push.setParent(lastPush.getParent());
		    else
			push.setParent(lastPush.getId());
		}
		bean.getPushList().add(push);
		System.out.println(ToStringBuilder.reflectionToString(push));
	    });
	    // bean.setPushs(bean.getPushList().size());

	    // System.out.println(doc.select("#main-content").get(0).ownText());

	    // metaline.get(3).childNodes().forEach(System.out::println);

	    System.out.println(ToStringBuilder.reflectionToString(bean));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// return bean;
    }

    public Push parsePush(String html) {
	Push result = new Push();

	return result;
    }
}
