package com.uri.webcrawler;

import java.util.concurrent.BlockingQueue;

class UrlProcessor implements Runnable {

    private BlockingQueue<Crawler.UrlProcessData> urlProcessingQueue;
    private WebPageGraph graph;

    public UrlProcessor(BlockingQueue<Crawler.UrlProcessData> urlProcessingQueue, WebPageGraph graph) {
        this.urlProcessingQueue = urlProcessingQueue;
        this.graph = graph;
    }

    @Override
    public void run() {
        // TODO:
    }
}
