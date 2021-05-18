package com.uri.webcrawler;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

class WebPage {
    private final String url;
    // TODO: Must be Concurrent?
    private ConcurrentHashMap<String, WebPage> linkedPages = new ConcurrentHashMap<>();

    WebPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Iterable<WebPage> getLinkedPages()
    {
        return (Iterable<WebPage>) linkedPages.values();
    }

    public void addLinkedPage(WebPage page) {
        this.linkedPages.putIfAbsent(page.url, page);
    }
}
