/**
 * 
 */
package net.mysparks.camellia.scumaster.model;

import java.util.Date;

/**
 * @author Irving
 *
 */
public class Ohlc {

    private String stock;
    private Date date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double change;
    private Double rate;
    private Long volume;

    public Ohlc(String stock, Date date, Double open, Double high, Double low, Double close, Double change, Double rate,
	    Long volume) {
	super();
	this.stock = stock;
	this.date = date;
	this.open = open;
	this.high = high;
	this.low = low;
	this.close = close;
	this.change = change;
	this.rate = rate;
	this.volume = volume;
    }
    public String getStock() {
        return stock;
    }
    public void setStock(String stock) {
        this.stock = stock;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Double getOpen() {
        return open;
    }
    public void setOpen(Double open) {
        this.open = open;
    }
    public Double getHigh() {
        return high;
    }
    public void setHigh(Double high) {
        this.high = high;
    }
    public Double getLow() {
        return low;
    }
    public void setLow(Double low) {
        this.low = low;
    }
    public Double getClose() {
        return close;
    }
    public void setClose(Double close) {
        this.close = close;
    }
    public Double getChange() {
        return change;
    }
    public void setChange(Double change) {
        this.change = change;
    }
    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public Long getVolume() {
        return volume;
    }
    public void setVolume(Long volume) {
        this.volume = volume;
    }

}
