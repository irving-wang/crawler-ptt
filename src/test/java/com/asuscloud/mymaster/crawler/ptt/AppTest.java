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
    public void testParse() throws UnsupportedEncodingException, IOException{
	Path path = Paths.get("D:\\workspace\\crawler-ptt\\src\\test\\resources\\M.1508992024.A.A4E.html");
	
	PttArticleParser parser = new PttArticleParser();
	
	Article bean = new Article();
	bean.setBoard("stock");
	bean.setUri("M.1508992024.A.A4E.html");
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
