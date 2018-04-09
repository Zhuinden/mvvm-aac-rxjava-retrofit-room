package com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Zhuinden on 2016.07.28..
 */
@Root(name = "image")
public class CatBO {
    @Element(name = "url")
    private String url;

    @Element(name = "id")
    private String id;

    @Element(name = "source_url")
    private String sourceUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}