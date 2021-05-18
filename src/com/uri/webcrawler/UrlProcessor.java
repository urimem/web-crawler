package com.uri.webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class UrlProcessor implements Runnable {

    // Crawler's new URL processing queue
    private final BlockingQueue<UrlProcessData> urlProcessingQueue;
    // Crawler's web page graph
    private final WebPageGraph graph;

    public UrlProcessor(BlockingQueue<UrlProcessData> urlProcessingQueue, WebPageGraph graph) {
        this.urlProcessingQueue = urlProcessingQueue;
        this.graph = graph;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Blocking queue will block the thread if empty
                var newPageData = urlProcessingQueue.take();
                if (!graph.contains(newPageData.newPageUrl)) {
                    // Create new web page in the graph
                    WebPage newPage = graph.add(newPageData.newPageUrl);
                    // Link the parent page to the new page
                    newPageData.parent.addLinkedPage(newPage);
                    // Retrieve and iterate linked pages
                    for (String url : getLinkedPages(newPage.getUrl())) {
                       if (graph.contains(url)) {
                           // Get existing page and add as linkedPage to newPage
                           newPage.addLinkedPage(graph.find(url));
                       }
                       else {
                           // Add to new url to urlProcessingQueue
                           urlProcessingQueue.add(new UrlProcessData(newPage, url));
                       }
                    }

                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                        result.add(link.attr("href"));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
