package com.uri.webcrawler;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerTest {

    static final String SAMPLE_SITE_URL = "https://s3.amazonaws.com/bezmoog.com/index.html";
    static final String SAMPLE_SITE_PAGE_URL = "https://s3.amazonaws.com/bezmoog.com/hippopotamus.html";
    static final boolean DOMAIN_LIMIT = true;
    static final long CRAWLER_TIMEOUT = 6000;
    static final long CRAWLER_NO_TIMEOUT = 0;

    @Test
    void crawlSampleSite() {
        Crawler crawler = new Crawler();
        try {
            crawler.start(SAMPLE_SITE_URL, DOMAIN_LIMIT, CRAWLER_TIMEOUT);
            int pagesCount = crawler.webPageGraph.size();
            assertEquals(12, pagesCount);
            WebPage page = null;
            page = crawler.webPageGraph.find(SAMPLE_SITE_PAGE_URL);
            assertEquals(SAMPLE_SITE_PAGE_URL, page.getUrl());
        } catch (MalformedURLException e) {
            assertTrue(false);  // URL is verified
        }
    }
}