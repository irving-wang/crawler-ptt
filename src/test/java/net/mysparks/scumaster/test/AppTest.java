package net.mysparks.scumaster.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import net.mysparks.camellia.scumaster.model.Article;
import net.mysparks.camellia.scumaster.parser.PttArticleParser;
import net.mysparks.camellia.scumaster.parser.PttParseException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    // @Test
    public void testParse() throws UnsupportedEncodingException, IOException, ParseException, PttParseException {
	Path path = Paths.get("C:\\data\\crawl\\root\\www.ptt.cc\\bbs\\Stock", "M.1426130171.A.0AC.html");

	PttArticleParser parser = new PttArticleParser();

	Article bean = new Article();
	bean.setBoard("stock");
	bean.setUri(path.getFileName().toString());

	parser.parseArticle(new String(Files.readAllBytes(path), "utf-8"), bean);

	// System.out.println(bean.getContent());
	Assert.assertNotNull(bean.getContent());

    }

    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);

    // @Test
    public void testDate() throws ParseException {
	System.out.println(format.parse("Sat May 7 10:37:01 2016"));
    }

//    @Test
    public void testRename() throws IOException {
	Path dir = Paths.get("E:\\workspace\\scu-master\\working\\other");
	try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);) {
	    int count = 0;
	    for (Path file : dirStream) {
		if (!Files.isDirectory(file)) {
		    count++;
		    String[] names = file.getFileName().toString().substring(10, 23).split("_");
		    String newName = file.getFileName().toString().substring(0, 10) + names[1] + "_" + names[0] + ".csv";
		    Path target = file.getParent().resolve(newName);
		    System.out.println(count + " " + file + " <=> " + target);

		    Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
		    if (count > 1)
			return;
		}
	    }
	}
    }
}
