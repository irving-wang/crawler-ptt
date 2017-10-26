/**
 * 
 */
package com.asuscloud.mymaster.crawler.ptt;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author Irving
 *
 */
public class CrawlerConfiguration {

    public static String DATASOURCE_URL;
    public static String DATASOURCE_DRIVECLASS;
    public static String DATASOURCE_USER;
    public static String DATASOURCE_PASSWORD;
    public static String LOG_CONFIG;
    public static String LOG_OUTPUT;
    public static String RAWDATD_STORAGE;
    public static String BACKUP_STORAGE;

    public static void loadConfiguration() throws ConfigurationException {
	Configuration config = new Configurations().xml(new File(System.getProperty("crawler.config")));
	
	LOG_CONFIG = config.getString("config.log");
	LOG_OUTPUT = config.getString("directory.logging_root");
	RAWDATD_STORAGE = config.getString("directory.rawdata_storage");
	BACKUP_STORAGE = config.getString("directory.backup_storage");
	DATASOURCE_URL = config.getString("persistence.datasource_url");
	DATASOURCE_DRIVECLASS = config.getString("persistence.driver_class_name");
	DATASOURCE_USER = config.getString("persistence.username");
	DATASOURCE_PASSWORD = config.getString("persistence.password");

    }
}
