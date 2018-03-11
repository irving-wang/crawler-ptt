package net.mysparks.camellia.scumaster;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.util.ContextInitializer;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import net.mysparks.camellia.scumaster.dao.ArticleDao;
import net.mysparks.camellia.scumaster.model.Article;
import net.mysparks.camellia.scumaster.parser.PttArticleParser;
import net.mysparks.camellia.scumaster.parser.PttParseException;

/**
 * 
 *
 */
public class PttCrawlerApp {
    
    public static void main(String[] args) throws Exception {
	CrawlerConfiguration.loadConfiguration();
	
	System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, CrawlerConfiguration.LOG_CONFIG);
//	setupLog();
	
	for (int i = 0; i < args.length; i++) {
	    if (args[i].equals("-crawler")) {
		new PttCrawlerApp().pullPage();
	    } else if (args[i].equals("-import")) {
		new PttCrawlerApp().importToDB();
	    }
	}
    }

    public void importToDB() throws Exception {
	Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	Path backup = Paths.get(CrawlerConfiguration.BACKUP_STORAGE);
	Path rawdata = Paths.get(CrawlerConfiguration.RAWDATD_STORAGE);
	Path irregular = Paths.get(CrawlerConfiguration.IRREGULAR_STORAGE);
	Files.createDirectories(backup);
	Files.createDirectories(rawdata);
//	PttBbsDao dao = new PttBbsDao();
	ArticleDao dao = new ArticleDao();
	PttArticleParser parser = new PttArticleParser();
	for (Path path : Files.newDirectoryStream(rawdata)) {
	    if (Files.isDirectory(path) || path.getFileName().toString().startsWith("index"))
		continue;
	    
	    Article bean = new Article();
	    try {
		bean.setBoard("stock");
		bean.setParent_uri("");
		bean.setUri(path.getFileName().toString());
		parser.parseArticle(new String(Files.readAllBytes(path), "utf-8"), bean);

		if (bean.getIp() != null) {
		    int c = dao.insertArticleAndPush(bean);
		    if (c > 0) {
			Files.move(path, backup.resolve(path.getFileName()));
		    }
		} else {
		    log.error("Article hasn't IP. {}", bean.getUri());
		    Files.move(path, irregular.resolve(path.getFileName()));
		}
	    } catch (PttParseException e) {
		log.error("Article parse fail: {} {}", e.getMessage(), bean.getUri());
		Files.move(path, irregular.resolve(path.getFileName()));
	    } catch (Exception e) {
		log.error("Parse fail. {} {}", e.getMessage(), bean.getUri());
	    }

	}
    }

    public void pullPage() throws Exception {
	
	CrawlConfig config = new CrawlConfig();
	config.setCrawlStorageFolder(CrawlerConfiguration.CRAWL_STORAGE_ROOT);
	config.setMaxDepthOfCrawling(CrawlerConfiguration.MAX_DEPTH_OF_CRAWLING);

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
//	controller.addSeed("https://www.ptt.cc/bbs/Stock/");
	for(String pageUrl: CrawlerConfiguration.SEED_PAGES)
	    controller.addSeed(pageUrl);

	/*
	 * Start the crawl. This is a blocking operation, meaning that your code
	 * will reach the line after this only when crawling is finished.
	 */
	controller.start(DocWebCrawler.class, CrawlerConfiguration.NUMBER_OF_CRAWLERS);
    }
    
//    static void setupLog() {
//	LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//	try {
//	    JoranConfigurator configurator = new JoranConfigurator();
//	    configurator.setContext(context);
//	    // Call context.reset() to clear any previous configuration, e.g. default
//	    // configuration. For multi-step configuration, omit calling context.reset().
//	    context.reset();
//	    configurator.doConfigure(CrawlerConfiguration.LOG_CONFIG);
//	} catch (JoranException je) {
//	    // StatusPrinter will handle this
//	}
//	StatusPrinter.printInCaseOfErrorsOrWarnings(context);
//
//	log.info("Entering application.");
//    }
}
