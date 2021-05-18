package com.uri.webcrawler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

// Single website (single root page) web crawler
// Using only linked pages (a href)
public class Crawler {

    private WebPageGraph webPageGraph;
    // Url processing queue is unbounded - consider limiting & handling spill
    BlockingQueue<UrlProcessData> urlProcessingQueue = new LinkedBlockingDeque<UrlProcessData>();

    private class UrlProcessData{
        public WebPage parent;
        public String newPageUrl;

        UrlProcessData(WebPage parent, String newPageUrl) {
            this.parent = parent;
            this.newPageUrl = newPageUrl;
        }
    }

    public void start(String rootPageUrl) {
        this.webPageGraph = new WebPageGraph(rootPageUrl);
        urlProcessingQueue.add(new UrlProcessData(null, rootPageUrl));

    }


}
