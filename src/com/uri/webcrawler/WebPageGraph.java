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

    //contains

    //add

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
            result.append(indent).append(page.getUrl()).append('\n');
            printedPageUrls.add(page.getUrl());
            for (Iterator<WebPage> it = page.getLinkedPages(); it.hasNext(); ) {
                WebPage child = it.next();
                graphTraverse(printedPageUrls, result,child, indent+'\t');
            }
        }
    }
}
