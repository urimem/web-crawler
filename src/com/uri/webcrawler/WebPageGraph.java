package com.uri.webcrawler;

import java.util.concurrent.ConcurrentHashMap;

public class WebPageGraph {
    // Key = Full URL string
    // TBD: remove the protocol
    private ConcurrentHashMap<String, WebPage> pages = new ConcurrentHashMap<>();
    private final String rootPageUrl;

    public WebPageGraph(String rootPageUrl) {
        this.rootPageUrl = rootPageUrl;
    }

    //public void

}
