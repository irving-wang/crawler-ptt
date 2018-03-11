/**
 * 
 */
package net.mysparks.camellia.scumaster.parser;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mysparks.camellia.scumaster.model.Article;
import net.mysparks.camellia.scumaster.model.Push;

/**
 * @author Irving
 *
 */
public class PttArticleParser {
    private  Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);
    private SimpleDateFormat pushFormat = new SimpleDateFormat("MM/dd HH:mm yyyy", Locale.US);

    public void parseArticle(String html, Article bean) throws ParseException, PttParseException {
	log.info("Parse: {}", bean.getUri());
	Document doc = Jsoup.parse(html);// Jsoup.connect("http://www.ptt.cc/bbs/Stock/").get();

	// Elements metaline = doc.select("div .article-metaline");
	Element mainContent = doc.select("#main-content").get(0);
	try {
	    Elements metalines = mainContent.select(".article-meta-value");
	    // doc.select("span.f2").forEach(System.out::println);
	    String author = metalines.get(0).text();
	    bean.setUserid(author.substring(0, author.indexOf("(")).trim());
	    bean.setNickname(author.substring(author.indexOf("(") + 1, author.lastIndexOf(")")).trim());
	    bean.setTitle(metalines.get(2).text());
	    bean.setItem_date(format.parse(metalines.get(3).text()));
	} catch (Exception e) {
	    throw new PttParseException("[Ptt] parse metaline fail.", e);
	}

	bean.setContent(mainContent.text());
	String[] conts = bean.getContent().split("※ 發信站:");
	bean.setContent(conts[0]);

	String f2= doc.select(".f2").text();
	int fs = -1;
	if ((fs = f2.indexOf("來自:")) > 0) {
	    fs += "來自:".length();
	} else if ((fs = f2.indexOf("From:")) > 0) {
	    fs += "From:".length();
	}
	if (fs >= 0) {
//	    f2 = f2.substring(fs);
	    int fe = f2.indexOf("(", fs);
	    int fe2 = f2.indexOf("※", fs);
	    if (fe < 0 || (fe2 > 0 && fe2 < fe))
		fe = fe2;
//	    System.out.println("fs="+fs+",fe="+fe);
//	    System.out.println("f2="+f2);
	    bean.setIp(f2.substring(fs, fe).trim());
//	    System.out.println("ip="+bean.getIp());
	    if (bean.getIp().length() > 15)
		throw new RuntimeException(bean.getUri()+" parse fail, Ip: "+bean.getIp());
	}
	// System.out.println("conts1="+conts[1]);
//	int fend = conts[1].indexOf("※ 文章網址");
//	if (fend > 0) {
//	    String footer = conts[1].substring(0, fend);// mainContent.select("span.f2").get(0).text();
//	    bean.setIp(footer.substring(footer.lastIndexOf(":") + 1, footer.length()).trim());
//	}else{
//	    
//	}

	Calendar cal = Calendar.getInstance();
	cal.setTime(bean.getItem_date());
	

	bean.setPushs(0);
	bean.setPushList(new LinkedList<Push>());
	Elements e = mainContent.select(".push");
	e.forEach(o -> {
//	     o.children().forEach(System.out::println);
	    try {
		bean.setPushs(bean.getPushs() + 1);
		Push push = new Push();
		push.setId(bean.getPushs());
		push.setBoard(bean.getBoard());
		push.setTag(o.select(".push-tag").text().trim());
		push.setUserid(o.select(".push-userid").text().trim());
		push.setContent(o.select(".push-content").text().substring(1).trim());// .replace(":", "")

		push.setItem_date(
			pushFormat.parse(o.select(".push-ipdatetime").text().trim() + " " + cal.get(Calendar.YEAR)));

		push.setPost_id(bean.getUri());
		bean.getPushList().add(push);
	    } catch (ParseException e1) {
		String txt = o.children().stream().collect(Collectors.toList()).toString();// o.children().forEach(System.out::println);
		log.warn("Parse error, skip it: " + txt, e1);
	    } catch(Exception e1) {
		log.warn("Parse error, skip it: " + o, e1);
	    }
//	    if (push.getTag().equals("→")) {
//		Push lastPush = bean.getPushList().get(bean.getPushList().size() - 1);
//		if (lastPush.getParent() > 0)
//		    push.setParent(lastPush.getParent());
//		else
//		    push.setParent(lastPush.getId());
//	    }
//	    bean.getPushList().add(push);
//	    System.out.println(ToStringBuilder.reflectionToString(push));
	});
	// bean.setPushs(bean.getPushList().size());

	// System.out.println(doc.select("#main-content").get(0).ownText());

	// metaline.get(3).childNodes().forEach(System.out::println);

//	System.out.println(ToStringBuilder.reflectionToString(bean));
	// return bean;
    }

    public Push parsePush(String html) {
	Push result = new Push();

	return result;
    }
}
