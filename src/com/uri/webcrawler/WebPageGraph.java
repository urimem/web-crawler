package com.uri.webcrawler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebPageGraph {
    // Key = Full URL string
    // TODO: remove the schema/protocol part, remove #fragment part
    private ConcurrentHashMap<String, WebPage> pages = new ConcurrentHashMap<>();
    private final String rootPageUrl;

    public WebPageGraph(String rootPageUrl) {
        this.rootPageUrl = rootPageUrl;
    }

    public boolean contains(String url) {
        return pages.containsKey(url);
    }

    public WebPage find(String url) {
        return pages.get(url);
    }

    public WebPage add(String url) {
        if (pages.containsKey(url))
            return pages.get(url);
        else {
            var newPage = new WebPage(url);
            pages.put(url, newPage);
            return newPage;
        }
    }

    // TODO: get JSON output
    @Override
    public String toString() {
        Set<String> printedPageUrls = new HashSet<String>();
        StringBuffer result = new StringBuffer();
        graphTraverse(printedPageUrls, result,pages.get(rootPageUrl), "");
        return result.toString();
    }

    // Recursive print method - DFS traverse, not drilling to child pageas if page already printed.
    private void graphTraverse(Set<String> printedPageUrls, StringBuffer result, WebPage page, String indent) {
        if (printedPageUrls.contains(page.getUrl())) {
            result.append(indent).append(page.getUrl()).append('\n');
        }
        else {
            // page not printed yet
            result.append(indent).append(page.getUrl()).append('\n');
            printedPageUrls.add(page.getUrl());
            for (WebPage child : page.getLinkedPages()) {
                graphTraverse(printedPageUrls, result,child, indent+'\t');
            }
        }
    }
}
