package com.uri.webcrawler;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

// Single website (single root page) web crawler
// Scraping linked pages (a href)
public class Crawler {

    private final int MAX_CONCURRENT_PROCESSORS = 10;
    private final int THREAD_FINISH_DELAY = 3;          // Used for termination process
    final WebPageGraph webPageGraph = new WebPageGraph();
    BlockingQueue<UrlProcessData> urlProcessingQueue = new LinkedBlockingDeque<>(); // Unbound - consider limiting & handling spill

    public void start(String rootPageUrl, boolean limitToRootDomain, long timeout) throws MalformedURLException {
        String domain = null;
        if (limitToRootDomain) {
            URL uri = new URL(rootPageUrl);
            domain = uri.getHost();
        }
        if (timeout < 0) {
            timeout = 0;
        }
        // Adding the root page to the queue as the first URL processing job
        urlProcessingQueue.add(new UrlProcessData(null, rootPageUrl));

        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(MAX_CONCURRENT_PROCESSORS);
        for (int i = 0; i < MAX_CONCURRENT_PROCESSORS; i++ ) {
            var urlProcessor = new UrlProcessor(urlProcessingQueue, webPageGraph, domain, i);
            executor.submit(urlProcessor);
        }

        executor.shutdown(); // Not getting new tasks
        try {
            executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            //boolean finishedOk = executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);   // DEBUG
            //if (!finishedOk) System.out.println("Crawler timeout reached.");                  // DEBUG
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(THREAD_FINISH_DELAY, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                executor.awaitTermination(THREAD_FINISH_DELAY, TimeUnit.SECONDS);
                    //System.err.println("Pool did not terminate");     // DEBUG
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        //System.out.println("Crawler - END - Queue size:" + urlProcessingQueue.size());    // DEBUG
    }

    public JSONObject getGraph() {
        return webPageGraph.toJSON();
    }
}
