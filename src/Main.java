
import com.uri.webcrawler.Crawler;

import java.net.MalformedURLException;

public class Main {

    static final String DEFAULT_ROOT_URL = "https://s3.amazonaws.com/bezmoog.com/index.html"; //"https://www.baeldung.com";  // "https://www.worldtimebuddy.com";  // "http://www.columbia.edu/~fdc/sample.html";
    static final boolean DOMAIN_LIMIT = true;
    static final long CRAWLER_TIMEOUT = 6000;

    // Parameter 1: String - root crawling URL
    // Parameter 2: boolean - limit domain to the root domain - will not be added or processed. Default true.
    // Parameter 3: long - milliseconds crawler timeout, for processing big websites. 0 = no timeout
    public static void main(String[] args) {

        var crawler = new Crawler();
        try {
            switch (args.length) {
                case 0:
                    crawler.start(DEFAULT_ROOT_URL, DOMAIN_LIMIT, CRAWLER_TIMEOUT);
                    break;
                case 1:
                    crawler.start(args[0], DOMAIN_LIMIT, CRAWLER_TIMEOUT);
                    break;
                case 2:
                    crawler.start(args[0], Boolean.parseBoolean(args[1]), CRAWLER_TIMEOUT);
                    break;
                default:
                    crawler.start(args[0], Boolean.parseBoolean(args[1]), Long.parseLong(args[2]));
            }

            System.out.println("Main - Graph JSON printout:");
            System.out.println(crawler.getGraph());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
