package com.uri.webcrawler;

import java.util.concurrent.ConcurrentHashMap;

public class WebPage {
    private final String url;
    // TBD: Must be Concurrent?
    private ConcurrentHashMap<String, WebPage> linkedPages = new ConcurrentHashMap<>();

    private WebPage(String url) {
        this.url = url;
    }

    public void addLinkedPage(WebPage page) {
        this.linkedPages.putIfAbsent(page.url, page);
    }
}
