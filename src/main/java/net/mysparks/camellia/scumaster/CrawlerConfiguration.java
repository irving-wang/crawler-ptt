/**
 * 
 */
package net.mysparks.camellia.scumaster;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author Irving
 *
 */
public class CrawlerConfiguration {

    public static Configuration config;
    public static String DATASOURCE_URL;
    public static String DATASOURCE_DRIVECLASS;
    public static String DATASOURCE_USER;
    public static String DATASOURCE_PASSWORD;
    public static String LOG_CONFIG;
    public static String LOG_OUTPUT;
    public static String RAWDATD_STORAGE;
    public static String BACKUP_STORAGE;
    public static String IRREGULAR_STORAGE;
    public static String CRAWL_STORAGE_ROOT;
    
    public static int NUMBER_OF_CRAWLERS;
    public static int MAX_DEPTH_OF_CRAWLING;
    public static List<String> SEED_PAGES;
    public static List<String> FILTER_URLS;
    public static List<String> FILTER_PATHS;
    public static List<String> FILTER_ANCHORS;
    public static List<String> VISIT_URLS;
    
    public static String MYBATIS_CONFIG;

    public static void loadConfiguration() throws ConfigurationException {
	config = new Configurations().xml(new File(System.getProperty("crawler.config")));
	
	LOG_CONFIG = config.getString("config.log");
	MYBATIS_CONFIG = config.getString("config.mybatis");
	
	LOG_OUTPUT = config.getString("directory.logging_root");
	RAWDATD_STORAGE = config.getString("directory.rawdata_storage");
	BACKUP_STORAGE = config.getString("directory.backup_storage");
	IRREGULAR_STORAGE = config.getString("directory.irregular_storage");
	CRAWL_STORAGE_ROOT = config.getString("directory.crawl_storage_root");
	
	DATASOURCE_URL = config.getString("persistence.datasource_url");
	DATASOURCE_DRIVECLASS = config.getString("persistence.driver_class_name");
	DATASOURCE_USER = config.getString("persistence.username");
	DATASOURCE_PASSWORD = config.getString("persistence.password");
	
	NUMBER_OF_CRAWLERS = config.getInt("crawler4j.number_of_crawlers");
	MAX_DEPTH_OF_CRAWLING = config.getInt("crawler4j.max_depth_of_crawling", -1);
	SEED_PAGES = config.getList(String.class, "crawler4j.seeds.page", Collections.emptyList());
	FILTER_ANCHORS = config.getList(String.class, "crawler4j.filters.anchor", Collections.emptyList());
	FILTER_URLS = config.getList(String.class, "crawler4j.filters.url", Collections.emptyList());
	FILTER_PATHS = config.getList(String.class, "crawler4j.filters.path", Collections.emptyList());
	VISIT_URLS = config.getList(String.class, "crawler4j.visits.url", Collections.emptyList());
	
	ConfigurationUtils.dump(config, System.out);
	System.out.println();
    }
    
}
