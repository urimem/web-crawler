package com.uri.webcrawler;

// Used to queue page processing jobs
// Used by UrlProcessor when finding new pages and used by Crawler for the root page
class UrlProcessData{
    private WebPage parent;
    private String newPageUrl;

    public WebPage getParent() {
        return parent;
    }

    public String getNewPageUrl() {
        return newPageUrl;
    }

    UrlProcessData(WebPage parent, String newPageUrl) {
        this.parent = parent;
        this.newPageUrl = newPageUrl;
    }
}

