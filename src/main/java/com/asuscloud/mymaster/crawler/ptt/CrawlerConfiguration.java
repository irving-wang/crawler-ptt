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
    
    public static Configuration config;
    public static String DATASOURCE_URL = config.getString("persistence.datasource_url");
    public static String DATASOURCE_DRIVECLASS = config.getString("persistence.driver_class_name");
    public static String DATASOURCE_USER = config.getString("persistence.username");
    public static String DATASOURCE_PASSWORD = config.getString("persistence.password");
    
    static{
	Configurations configs = new Configurations();
	try
	{
	    config = configs.xml(new File(System.getProperty("crawler.config")));
	}
	catch (ConfigurationException cex)
	{
	    cex.printStackTrace();
	}
    }
}
