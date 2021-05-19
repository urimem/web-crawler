package com.uri.webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

// Single website (single root page) web crawler
// Scraping linked pages (a href)
public class Crawler {

    private final int MAX_CONCURRENT_PROCESSORS = 30;
    private final WebPageGraph webPageGraph = new WebPageGraph();                   // In memory graph
    BlockingQueue<UrlProcessData> urlProcessingQueue = new LinkedBlockingDeque<>(); // Unbound - consider limiting & handling spill

    public void start(String rootPageUrl, boolean limitToRootDomain) throws MalformedURLException {
        String domain = null;
        if (limitToRootDomain) {
            URL uri = new URL(rootPageUrl);
            domain = uri.getHost();
        }
        // Adding the root page to the queue as the first URL processing job
        urlProcessingQueue.add(new UrlProcessData(null, rootPageUrl));

        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(MAX_CONCURRENT_PROCESSORS);
        for (int i = 0; i < MAX_CONCURRENT_PROCESSORS; i++ ) {
            var urlProcessor = new UrlProcessor(urlProcessingQueue, webPageGraph, domain, i);
            executor.submit(urlProcessor);
        }

        // TODO: better check that threads have finished also...
        // executor.getQueue().iterator()

        boolean active = true;
        while (active) {
            try {

                System.out.println("Queue size:" + urlProcessingQueue.size());
                if (urlProcessingQueue.size() == 0) {
                    Thread.sleep(5000);
                    if (urlProcessingQueue.size() == 0) {
                        active = false;                        // disable for debug
                    }
                } else
                    Thread.sleep(4000);


                Thread.sleep(10000);
                active = false;
                //((LinkedBlockingDeque)urlProcessingQueue).clear();


            } catch (InterruptedException e) {
                //Log.write("URL file not found or interrupt exception");
                e.printStackTrace();
            }
        }

        System.out.println("Initiating shutdown");
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        //executor.shutdown();
        System.out.println("Queue size:" + urlProcessingQueue.size());
        System.out.println("URL Graph print:");
        System.out.println(webPageGraph);
    }
}
