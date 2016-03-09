
package com.linkensky.revice.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Users {

    private Integer total;
    private Integer perPage;
    private Integer currentPage;
    private Integer lastPage;
    private String nextPageUrl;
    private Object prevPageUrl;
    private Integer from;
    private Integer to;
    private List<UserItem> data = new ArrayList<UserItem>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The perPage
     */
    public Integer getPerPage() {
        return perPage;
    }

    /**
     * 
     * @param perPage
     *     The per_page
     */
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    /**
     * 
     * @return
     *     The currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * 
     * @param currentPage
     *     The current_page
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 
     * @return
     *     The lastPage
     */
    public Integer getLastPage() {
        return lastPage;
    }

    /**
     * 
     * @param lastPage
     *     The last_page
     */
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * 
     * @return
     *     The nextPageUrl
     */
    public String getNextPageUrl() {
        return nextPageUrl;
    }

    /**
     * 
     * @param nextPageUrl
     *     The next_page_url
     */
    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    /**
     * 
     * @return
     *     The prevPageUrl
     */
    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    /**
     * 
     * @param prevPageUrl
     *     The prev_page_url
     */
    public void setPrevPageUrl(Object prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    /**
     * 
     * @return
     *     The from
     */
    public Integer getFrom() {
        return from;
    }

    /**
     * 
     * @param from
     *     The from
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * 
     * @return
     *     The to
     */
    public Integer getTo() {
        return to;
    }

    /**
     * 
     * @param to
     *     The to
     */
    public void setTo(Integer to) {
        this.to = to;
    }

    /**
     * 
     * @return
     *     The data
     */
    public List<UserItem> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<UserItem> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
