/**
 * 
 */
package net.mysparks.camellia.scumaster;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Irving
 *
 */
public class DocWebCrawler extends WebCrawler {
    private Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
	    						   + "|png|mp3|mp4|zip|gz))$");
    
    private static Path storageFolder;
    static {
        storageFolder = Paths.get(CrawlerConfiguration.CRAWL_STORAGE_ROOT);
        if (!Files.exists(storageFolder)) {
            try {
		Files.createDirectories(storageFolder);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
        }
    }

    /**
     * This method receives two parameters. The first parameter is the page in
     * which we have discovered this new url and the second parameter is the new
     * url. You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic). In this example,
     * we are instructing the crawler to ignore urls that have css, js, git, ...
     * extensions and to only accept urls that start with
     * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
     * parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
//	System.out.println(ToStringBuilder.reflectionToString(url));

	if (FILTERS.matcher(url.getURL()).matches())
	    return false;

	for (String txt : CrawlerConfiguration.FILTER_ANCHORS) {
	    if (url.getAnchor() != null && url.getAnchor().startsWith(txt)) {
		return false;
	    }
	}
	for (String txt : CrawlerConfiguration.FILTER_URLS) {
	    if (url.getURL() != null && url.getURL().startsWith(txt)) {
		return false;
	    }
	}
	for (String txt : CrawlerConfiguration.FILTER_PATHS) {
	    if (url.getPath() != null && url.getPath().startsWith(txt)) {
		return false;
	    }
	}

	for (String txt : CrawlerConfiguration.VISIT_URLS) {
	    if (url.getURL() != null && url.getURL().startsWith(txt)) {
		return true;
	    }
	}
	
	return false;
//	return !FILTERS.matcher(url.getURL()).matches()
////		&& !url.getPath().equals("/bbs/Stock/index1.html")
//		&& (
////			url.getDomain().equals("ptt.cc") ||
//			url.getURL().startsWith("https://www.ptt.cc/bbs/Stock") 
//			|| url.getURL().startsWith("http://www.ptt.cc/bbs/Stock")
//			);
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
	String url = page.getWebURL().getURL();
	log.debug("URL: {}", url);

	if (page.getParseData() instanceof HtmlParseData) {
	    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	    String text = htmlParseData.getText();
	    String html = htmlParseData.getHtml();
	    Set<WebURL> links = htmlParseData.getOutgoingUrls();

	    log.trace("Text length: {}", text.length());
	    log.trace("Html length: {}", html.length());
	    log.trace("Number of outgoing links: {}", links.size());
	    
//	    log.trace(ToStringBuilder.reflectionToString(page.getWebURL()));

	    WebURL webUrl = page.getWebURL();
//	    Path htmlPath = storageFolder.resolve(page.getWebURL().getDomain()).resolve(page.getWebURL().getDocid()+".html");
	    Path htmlPath = storageFolder.resolve(webUrl.getSubDomain() + "." + webUrl.getDomain());
	    htmlPath = Paths.get(htmlPath.toString(), page.getWebURL().getPath());
//	    System.out.println("HtmlPath: "+htmlPath);
	    
	    try {
		Files.createDirectories(htmlPath.getParent());
		
		Files.write(htmlPath, html.getBytes("UTF-8"), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
	    } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
