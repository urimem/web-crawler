
import com.uri.webcrawler.Crawler;

public class Main {

    static final String DEFAULT_ROOT_URL = "https://www.baeldung.com"; // "https://www.worldtimebuddy.com";  // "http://www.columbia.edu/~fdc/sample.html";
    static final boolean DOMAIN_LIMIT = true;

    // Parameter 1: String root crawling URL
    // Parameter 2: boolean limit domain to the root domain - will not be added or processed
    public static void main(String[] args) {

        var crawler = new Crawler();

        if (args.length == 0) {
            crawler.start(DEFAULT_ROOT_URL, DOMAIN_LIMIT);
        } else {
            if (args.length >= 2) {
                crawler.start(args[0], Boolean.parseBoolean(args[1]));
            }
            else {
                crawler.start(args[0], DOMAIN_LIMIT);
            }
        }
    }
}
