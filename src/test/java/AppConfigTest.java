import com.buildit.AppConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppConfigTest extends BaseTest {

    @Test
    public void getConfig(){
        AppConfig config = AppConfig.getInstance();
        Assert.assertNotNull(config);
    }
}
