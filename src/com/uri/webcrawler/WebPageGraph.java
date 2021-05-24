package com.uri.webcrawler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebPageGraph {
    // Key = Full URL string
    private ConcurrentHashMap<String, WebPage> pages = new ConcurrentHashMap<>();
    private WebPage rootPage;

    public boolean contains(String url) {
        return pages.containsKey(url);
    }

    public WebPage find(String url) {
        return pages.get(url);
    }

    public int size() {
        return pages.size();
    }
    public WebPage add(String url) {
        if (pages.containsKey(url))
            return pages.get(url);
        else {
            var newPage = new WebPage(url);
            pages.put(url, newPage);
            if (rootPage == null) {
                rootPage = newPage;
            }
            return newPage;
        }
    }

    public JSONObject toJSON() {
        Set<String> addedPageUrls = new HashSet<>();
        if (rootPage == null) {
            return new JSONObject("url", "Graph is empty");
        }
        return buildJSON(addedPageUrls, rootPage);
    }

    // Recursive
    private JSONObject buildJSON(Set<String> addedPageUrls, WebPage page) {
        JSONObject currentPage = new JSONObject();
        currentPage.put("url", page.getUrl());
        if (addedPageUrls.contains(page.getUrl())) {
            currentPage.put("linkedPages", "...");
        }
        else {
            addedPageUrls.add(page.getUrl());
            // First time adding this page -> process linked pages
            JSONArray linkedPages = new JSONArray();
            for (WebPage child : page.getLinkedPages()) {
                linkedPages.put(buildJSON(addedPageUrls, child));
            }
            currentPage.put("linkedPages", linkedPages);
        }
        return currentPage;
    }

    @Override
    public String toString() {
        if (rootPage == null) {
            return "Graph is empty";
        }
        Set<String> printedPageUrls = new HashSet<>();
        StringBuffer result = new StringBuffer();
        graphTraverse(printedPageUrls, result, rootPage, "");
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