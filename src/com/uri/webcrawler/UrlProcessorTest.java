package com.uri.webcrawler;

import org.junit.jupiter.api.Test;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

class UrlProcessorTest {

    static final String SAMPLE_PAGE_URL_NO_LINKS = "https://s3.amazonaws.com/bezmoog.com/cat.html";
    static final String SAMPLE_PAGE_URL_ONE_LINK = "https://s3.amazonaws.com/bezmoog.com/hippopotamus.html";
    static final String DOMAIN = "s3.amazonaws.com";

    @Test
    void processSamplePage() {
        final WebPageGraph webPageGraph = new WebPageGraph();
        BlockingQueue<UrlProcessData> urlProcessingQueue = new LinkedBlockingDeque<>(); // Unbound - consider limiting & handling spill
        // Adding the root page to the queue as the first URL processing job
        urlProcessingQueue.add(new UrlProcessData(null, SAMPLE_PAGE_URL_NO_LINKS));

        UrlProcessor up = new UrlProcessor(urlProcessingQueue, webPageGraph, DOMAIN, 1);
        up.run();

        assertEquals(1, webPageGraph.size());
        WebPage thePage = webPageGraph.find(SAMPLE_PAGE_URL_NO_LINKS);
        assertEquals(SAMPLE_PAGE_URL_NO_LINKS, thePage.getUrl());
    }
}