import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void baseTestSetup(){
        System.setProperty("ext.properties.dir","env");
        System.setProperty("spring.profiles.active","dev");
    }
}
