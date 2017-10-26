package com.asuscloud.mymaster.crawler.ptt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.asuscloud.mymaster.crawler.ptt.model.Article;
import com.asuscloud.mymaster.crawler.ptt.parser.PttArticleParser;

/**
 * Unit test for simple App.
 */
public class AppTest {

    
    @Test
    public void testParse() throws UnsupportedEncodingException, IOException, ParseException{
	Path path = Paths.get("C:\\data\\crawl\\root\\www.ptt.cc\\bbs\\Stock", "M.1426130171.A.0AC.html");
	
	PttArticleParser parser = new PttArticleParser();
	
	Article bean = new Article();
	bean.setBoard("stock");
	bean.setUri(path.getFileName().toString());
	
	parser.parseArticle(new String(Files.readAllBytes(path), "utf-8"), bean);
	
//	System.out.println(bean.getContent());
	Assert.assertNotNull(bean.getContent());
	
    }
    
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);
    
//    @Test
    public void testDate() throws ParseException{
	System.out.println(format.parse("Sat May 7 10:37:01 2016"));
    }
}
