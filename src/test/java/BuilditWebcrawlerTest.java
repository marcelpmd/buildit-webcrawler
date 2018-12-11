import com.buildit.AppConfig;
import com.buildit.BuilditWebcrawler;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BuilditWebcrawlerTest extends BaseTest {

    private static String crawlUrlStr = "http://wiprodigital.com";
    private BuilditWebcrawler webcrawler = null;
    private URL crawlUrl = null;

    @BeforeSuite
    public void init() throws MalformedURLException {
        webcrawler = new BuilditWebcrawler(crawlUrlStr);
        crawlUrl = new URL(crawlUrlStr);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void constructorUrlNull() {
        String url = null;
        new BuilditWebcrawler(url);
    }

    @Test
    public void connectToUrl() throws IOException {
        Assert.assertNotNull(webcrawler.connectToUrl(crawlUrlStr));
    }

    @Test
    public void testInternalLinks() throws IOException {
        webcrawler.getCurrentLinks(crawlUrl);
        webcrawler.getInternalLinks().forEach( x -> Assert.assertTrue(x.contains(crawlUrl.getHost())));
    }

    @Test
    public void testExternalLinks() throws IOException {
        webcrawler.getCurrentLinks(crawlUrl);
        webcrawler.getExternalLinks().forEach( x -> Assert.assertFalse(x.contains(crawlUrl.getHost())));
    }
}
