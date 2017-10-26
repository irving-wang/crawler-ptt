package com.asuscloud.mymaster.crawler.ptt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.asuscloud.mymaster.crawler.ptt.db.PttBbsDao;
import com.asuscloud.mymaster.crawler.ptt.model.Article;
import com.asuscloud.mymaster.crawler.ptt.parser.PttArticleParser;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * 
 *
 */
public class PttCrawlerApp {
    public static void main(String[] args) throws Exception {
	CrawlerConfiguration.loadConfiguration();

	Path backup = Paths.get(CrawlerConfiguration.BACKUP_STORAGE);
	Path rawdata = Paths.get(CrawlerConfiguration.RAWDATD_STORAGE);
	PttBbsDao dao = new PttBbsDao();
	for (Path path : Files.newDirectoryStream(rawdata)) {
	    if (Files.isDirectory(path) || path.getFileName().toString().startsWith("index"))
		continue;

	    PttArticleParser parser = new PttArticleParser();

	    try {
		Article bean = new Article();
		bean.setBoard("stock");
		bean.setParent_uri("");
		bean.setUri(path.getFileName().toString());
		parser.parseArticle(new String(Files.readAllBytes(path), "utf-8"), bean);

		if (bean.getIp() != null) {
		    int c = dao.insertArticle(bean);
		    if (c > 0) {
			Files.move(path, backup.resolve(path.getFileName()));
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	}
    }

    public void runAppp() throws Exception {
	String crawlStorageFolder = "/data/crawl/root";
	int numberOfCrawlers = 7;

	CrawlConfig config = new CrawlConfig();
	config.setCrawlStorageFolder(crawlStorageFolder);

	/*
	 * Instantiate the controller for this crawl.
	 */
	PageFetcher pageFetcher = new PageFetcher(config);
	RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

	/*
	 * For each crawl, you need to add some seed urls. These are the first
	 * URLs that are fetched and then the crawler starts following links
	 * which are found in these pages
	 */
	// controller.addSeed("http://www.ics.uci.edu/~lopes/");
	// controller.addSeed("http://www.ics.uci.edu/~welling/");
	controller.addSeed("https://www.ptt.cc/bbs/Stock/");

	DocWebCrawler.configure(crawlStorageFolder);
	/*
	 * Start the crawl. This is a blocking operation, meaning that your code
	 * will reach the line after this only when crawling is finished.
	 */
	controller.start(DocWebCrawler.class, numberOfCrawlers);
    }
}
