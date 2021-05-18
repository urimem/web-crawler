package com.uri.webcrawler;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

class WebPage {
    private final String url;
    // TODO: Must be Concurrent?
    private ConcurrentHashMap<String, WebPage> linkedPages = new ConcurrentHashMap<>();

    private WebPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Iterator<WebPage> getLinkedPages()
    {
        return (Iterator<WebPage>) linkedPages.values();
    }

    public void addLinkedPage(WebPage page) {
        this.linkedPages.putIfAbsent(page.url, page);
    }
}
