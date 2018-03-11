/**
 * 
 */
package net.mysparks.camellia.scumaster.model;

/**
 * @author Irving
 *
 */
public class Stock {

    private String code;
    private String name;
    private String alias;
    private Integer sort;
    private String market;
    
    public Stock(String code, String name, String alias, Integer sort, String market) {
	super();
	this.code = code;
	this.name = name;
	this.alias = alias;
	this.sort = sort;
	this.market = market;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getMarket() {
        return market;
    }
    public void setMarket(String market) {
        this.market = market;
    }
    
}
