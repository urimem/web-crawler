package com.uri.webcrawler;

import java.util.concurrent.ConcurrentHashMap;

public class WebPageGraph {
    // Key = Full URL string
    // TODO: remove the schema/protocol part
    private ConcurrentHashMap<String, WebPage> pages = new ConcurrentHashMap<>();
    private final String rootPageUrl;

    public WebPageGraph(String rootPageUrl) {
        this.rootPageUrl = rootPageUrl;
    }

    //public void

}
