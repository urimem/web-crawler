package com.uri.webcrawler;

// Used to queue page processing jobs
// Used by UrlProcessor when finding new pages and used by Crawler for the root page
class UrlProcessData{
    public WebPage parent;
    public String newPageUrl;

    UrlProcessData(WebPage parent, String newPageUrl) {
        this.parent = parent;
        this.newPageUrl = newPageUrl;
    }
}

