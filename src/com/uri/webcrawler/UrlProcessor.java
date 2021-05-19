package com.uri.webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class UrlProcessor implements Runnable {

    // Crawler's new URL processing queue
    private final BlockingQueue<UrlProcessData> urlProcessingQueue;
    // Crawler's web page graph
    private final WebPageGraph graph;
    private String domainLimit;
    private int id;
    private boolean active = true;
    private final int POLL_TIMEOUT = 5000;      // Wait on empty queue before exit
    private final int MAX_JOBS = 20;            // Max jobs to be handled by processor

    public UrlProcessor(BlockingQueue<UrlProcessData> urlProcessingQueue, WebPageGraph graph, String domainLimit, int id) {
        this.urlProcessingQueue = urlProcessingQueue;
        this.graph = graph;
        this.domainLimit = domainLimit;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            int counter = 0; // TODO: replace ugly counter with main Crawler timeout
            while (active) {
                // urlProcessingQueue will block the thread if empty for POLL_TIMEOUT and then return null
                var newPageData = urlProcessingQueue.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                if (newPageData == null) {
                    this.active = false;
                }
                else {
                    System.out.println("UrlProcessor " + this.id + " URL:" + newPageData.newPageUrl);
                    if (!graph.contains(newPageData.newPageUrl)) {
                        // Create new web page in the graph
                        WebPage newPage = graph.add(newPageData.newPageUrl);
                        // Link the parent page to the new page
                        if (newPageData.parent != null) {
                            newPageData.parent.addLinkedPage(newPage);
                        }
                        // Retrieve and iterate linked pages
                        for (String url : getLinkedPages(newPage.getUrl())) {
                            if (graph.contains(url)) {
                                // Get existing page and add as linkedPage to newPage
                                newPage.addLinkedPage(graph.find(url));
                            } else {
                                // Add to new url to urlProcessingQueue
                                urlProcessingQueue.add(new UrlProcessData(newPage, url));
                            }
                        }
                    }
                }
                counter++;
                if (counter >= MAX_JOBS) {
                    active = false;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("UrlProcessor " + id + " interrupted" );
        }
    }

    private ArrayList<String> getLinkedPages(String url) {
        ArrayList<String> result = new ArrayList<>();
        try {
            // retrieve new web page
            URL pageUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) pageUrl.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            // TODO: handle other statuses - retry or back to queue
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                // Parse web page and extract all links
                Document doc = Jsoup.parse(content.toString());
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    if (link.attr("href").startsWith("htt")) {

                        String foundUrl = link.attr("href");
                        int index = foundUrl.lastIndexOf('#');
                        if (index != -1) {
                            foundUrl = foundUrl.substring(0, index);
                        }
                        if (domainLimit != null) {
                            if (foundUrl.contains((CharSequence)domainLimit)) {
                                result.add(foundUrl);
                            }
                        }
                        else {
                            result.add(foundUrl);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
