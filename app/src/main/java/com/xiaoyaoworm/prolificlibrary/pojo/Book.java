package com.xiaoyaoworm.prolificlibrary.pojo;

import java.util.HashMap;
import java.util.Map;
//import javax.annotation.Generated;

//@Generated("org.jsonschema2pojo")
public class Book {

    private String author;
    private String categories;
    private Integer id;
    private Object lastCheckedOut;
    private Object lastCheckedOutBy;
    private String title;
    private String url;
    private String publisher;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The categories
     */
    public String getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     * The categories
     */
    public void setCategories(String categories) {
        this.categories = categories;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The lastCheckedOut
     */
    public Object getLastCheckedOut() {
        return lastCheckedOut;
    }

    /**
     *
     * @param lastCheckedOut
     * The lastCheckedOut
     */
    public void setLastCheckedOut(Object lastCheckedOut) {
        this.lastCheckedOut = lastCheckedOut;
    }

    /**
     *
     * @return
     * The lastCheckedOutBy
     */
    public Object getLastCheckedOutBy() {
        return lastCheckedOutBy;
    }

    /**
     *
     * @param lastCheckedOutBy
     * The lastCheckedOutBy
     */
    public void setLastCheckedOutBy(Object lastCheckedOutBy) {
        this.lastCheckedOutBy = lastCheckedOutBy;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @param publisher
     * The publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}