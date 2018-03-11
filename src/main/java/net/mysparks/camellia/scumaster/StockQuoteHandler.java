/**
 * 
 */
package net.mysparks.camellia.scumaster;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.util.ContextInitializer;
import net.mysparks.camellia.scumaster.dao.StockDAO;
import net.mysparks.camellia.scumaster.model.Ohlc;
import net.mysparks.camellia.scumaster.model.Stock;

/**
 * @author Irving
 *
 */
public class StockQuoteHandler {
    
    static Logger log;
    static SimpleDateFormat scanDateFormat = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
    static DecimalFormat amountFormat = new DecimalFormat("##,###");
    static DecimalFormat priceFormat = new DecimalFormat("##,###.##");
    
    private void arrange(Path source, Path target) {

	try (InputStream input = Files.newInputStream(source); 
		OutputStream output = Files.newOutputStream(target);) {
	    AtomicInteger index = new AtomicInteger();
	    List<String> lines = IOUtils.readLines(input, "UTF-8").stream()
		    .filter(s -> index.incrementAndGet() > 1)
		    .map(s -> {
			String[] cols = s.split("[ \\s()]");
			// System.out.println(Arrays.toString(cols));
			return cols[1].trim().replace("　", "") + "," + cols[3].trim();
		    }).collect(Collectors.toList());
	    IOUtils.writeLines(lines, "\r\n", output, "UTF-8");

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void importStock(Path path) {
	StockDAO dao = new StockDAO();
	AtomicInteger sort = new AtomicInteger();
	try (InputStream input = Files.newInputStream(path);) {
	    IOUtils.readLines(input, "UTF-8")
	    .forEach(s -> {
		String[] cols = s.split(",");
		dao.insertStock(new Stock(cols[0].trim(), cols[1].trim(), null, sort.incrementAndGet(), "listed"));
	    });
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void importQuote(Path dir) throws IOException {
	StockDAO dao = new StockDAO();
	AtomicLong total = new AtomicLong();
	try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	    for (Path entry : stream) {
		if(Files.isDirectory(entry))
		    continue;
		if (entry.getFileName().toString().startsWith("STOCK_DAY")) {
		    try (InputStream input = Files.newInputStream(entry);) {
			String stock = entry.toString().split("[_\\.]")[3];
			AtomicInteger index = new AtomicInteger();
			IOUtils.readLines(input, "UTF-8").stream()
				.filter(s -> index.incrementAndGet() > 2)
				.forEach(s -> {
				    String[] cols = s.split("\"");// System.out.println(Arrays.toString(cols));
				    cols = Arrays.stream(cols)
					    .filter(c -> c.trim().length() > 0 && !c.trim().equals(","))
					    .toArray(String[]::new);
				    // System.out.println(Arrays.toString(cols));
				    if (cols.length >= 9) {
					try {
					    Date date = dateFormat.parse(cols[0]);
					    Calendar cal = Calendar.getInstance();
					    cal.setTime(date);
					    cal.add(Calendar.YEAR, 1911);

					    double change = priceFormat.parse(cols[7].substring(1)).doubleValue();
					    if (cols[7].substring(0, 1).equals("-"))
						change = 0.0 - change;
					    double close = priceFormat.parse(cols[6]).doubleValue();
					    double rate = Math.round((change / (change + close))*10000)/10000.0;

					    dao.insertQuote(new Ohlc(stock, cal.getTime(),
						    priceFormat.parse(cols[3]).doubleValue(),
						    priceFormat.parse(cols[4]).doubleValue(),
						    priceFormat.parse(cols[5]).doubleValue(),
						    close, change, rate,
						    amountFormat.parse(cols[8]).longValue()));
					} catch (Exception e) {
					    log.error("parse fail: " + s, e);
					}
				    }
				});
			log.info("Import {} quotes to DB from {}", index.get(), entry.getFileName());
			total.addAndGet(index.get());
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
	log.info("Import total {} quotes to DB in from {}", total.get(), dir);
    }
    
    /**
     * 
     * @param path
     * @param date
     * @param interval milliseconds
     */
    public void downloadQuote(Path path, String date, long interval) throws Exception{
	String urlPattern = "http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=csv&date=%s&stockNo=%s";
	
	AtomicInteger count = new AtomicInteger();
	try (InputStream input = Files.newInputStream(path);) {
	    IOUtils.readLines(input, "UTF-8").forEach(s -> {
		String stock = s.split(",")[1];
		Path file = path.getParent().resolve(String.format("STOCK_DAY_%s_%s.csv", date, stock));
//		log.info("{}", file);
		if (!Files.exists(file)) {
		    String url = String.format(urlPattern, date, stock);
		    try (InputStream resp = new URL(url).openStream();
			    OutputStreamWriter output = new OutputStreamWriter(
				    new FileOutputStream(file.toFile()), "UTF-8");) {
			IOUtils.copy(resp, output, "Big5");
			count.incrementAndGet();
			log.info("{} Fetch: {}", count, url);
			
			
			try {
			    if(count.get()%20==19)
				Thread.sleep(interval*5);
			    else
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			
		    } catch (IOException e) {
			log.error("Error: " + url, e);
			throw new RuntimeException(e);
		    }
		}
	    });
	} catch (IOException e) {
	    log.error("Read stock file fail.", e);
	}
    }

    public static void main(String[] args) throws Exception {
	
	CrawlerConfiguration.loadConfiguration();
	
	System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, CrawlerConfiguration.LOG_CONFIG);
	log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	StockQuoteHandler handler = new StockQuoteHandler();
	
	
	for (int i = 0; i < args.length; i++) {
	    if (args[i].equals("-arrange")) {
		String source = "E:\\workspace\\scu-master\\working\\stock.txt";
		String target = "E:\\workspace\\scu-master\\working\\stock_target.txt";
		handler.arrange(Paths.get(source), Paths.get(target));
	    }
	    else if (args[i].equals("-importStock")) {
		String target = "E:\\workspace\\scu-master\\stock_target.txt";
		handler.importStock(Paths.get(target));
	    }
	    else if (args[i].equals("-importQuote")) {
		String target = "E:\\workspace\\scu-master\\working";
		handler.importQuote(Paths.get(target));
	    }
	    else if (args[i].equals("-fetchQuote")) {
		String target = "E:\\workspace\\scu-master\\working\\stock_target.txt";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2000);
		end.set(Calendar.MONTH, Calendar.JANUARY);
		end.set(Calendar.DAY_OF_MONTH, 1);
		
		while (end.getTimeInMillis() < cal.getTimeInMillis()) {
		    handler.downloadQuote(Paths.get(target), scanDateFormat.format(cal.getTime()), 2000L);
		    cal.set(Calendar.DAY_OF_MONTH, 1);
		    cal.add(Calendar.DAY_OF_MONTH, -1);
		}
	    }
	}
	
    }
}
