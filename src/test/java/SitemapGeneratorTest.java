import com.buildit.AppConfig;
import com.buildit.SitemapGenerator;
import com.oracle.tools.packager.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SitemapGeneratorTest extends BaseTest {

    @Test
    public void generatorInitTest(){
        SitemapGenerator generator = new SitemapGenerator();
        Assert.assertNotNull(generator);
    }

    @Test
    public void writeToFileTest() throws IOException {
        String testStr = "test";
        SitemapGenerator generator = new SitemapGenerator();
        generator.writeToFile(testStr,true);
        generator.close();
        try (Stream<String> stream = Files.lines(Paths.get(generator.getOutputLoc()))) {
            stream.forEach(x -> Assert.assertTrue(x.equals(testStr)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
