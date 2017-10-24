/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Irving
 *
 */
public class DocWebCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
	    						   + "|png|mp3|mp4|zip|gz))$");
    
    private static Path storageFolder;
//    private static String[] crawlDomains;

    public static void configure(String storageFolderName) {
//        crawlDomains = domain;

        storageFolder = Paths.get(storageFolderName);
        
        if (!Files.exists(storageFolder)) {
            try {
		Files.createDirectories(storageFolder);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
        }
        
//	for (String crawlDomain : crawlDomains) {
//	    Path domainPath = storageFolder.resolve(Paths.get(crawlDomain));
//	    System.out.println(domainPath);
//	    if (!Files.exists(domainPath)) {
//	            try {
//			Files.createDirectories(domainPath);
//		    } catch (IOException e) {
//			e.printStackTrace();
//		    }
//	        }
//	}
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
	String href = url.getURL();
	
	return !FILTERS.matcher(href).matches()
		&& !url.getPath().equals("/bbs/Stock/index1.html")
		&& (
//			url.getDomain().equals("ptt.cc") ||
			href.startsWith("https://www.ptt.cc/bbs/Stock") 
			|| href.startsWith("http://www.ptt.cc/bbs/Stock")
			);
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
	String url = page.getWebURL().getURL();
	System.out.println("URL: " + url);

	if (page.getParseData() instanceof HtmlParseData) {
	    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	    String text = htmlParseData.getText();
	    String html = htmlParseData.getHtml();
	    Set<WebURL> links = htmlParseData.getOutgoingUrls();

	    System.out.println("Text length: " + text.length());
	    System.out.println("Html length: " + html.length());
	    System.out.println("Number of outgoing links: " + links.size());
	    
	    System.out.println(ToStringBuilder.reflectionToString(page.getWebURL()));

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
