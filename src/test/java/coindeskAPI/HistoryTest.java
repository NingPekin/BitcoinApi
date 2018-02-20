package coindeskAPI;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class HistoryTest {

        private static String host;

        @BeforeClass
        static public void setup(){
            Properties prop = getProperties();
            host = prop.getProperty("coindesk.url");
        }

        @Test
        public void
        price_resource_returns_200_with_errored_price()
        {

            given().log().uri().
                    when().
                    get(host+"/v1/bpi/historical/close.json?start={start}&end={end}",
                            "2018-02-01", "2018-02-01").
                    then().log().status().
                    log().body().statusCode(200).
                    body("bpi.2018-02-01", is(not(equalTo(9052))));
        }

         @Test
        public void
        price_resource_returns_200_with_expected_price()
        {
            // f--float define datatype
//            given().log().uri().
            given().
                    when().log().uri().
                    get(host+"/v1/bpi/historical/close.json?start={start}&end={end}",
                            "2018-02-01", "2018-02-02").
                    then().log().body().
                    statusCode(200).
                    body("bpi.2018-02-02", equalTo(8827.63f));
        }


        @Test
        public void price_resource_returns_200_with_expected_currency()
        {
            String[] currencies={"CAD","USD","CNY","NPR","MAD","UZS"};
            for (String c:currencies)
            {

                given().log().uri().when().
                        get(host+"/v1/bpi/historical/close.json?currency={currency}",c).then().log().body().statusCode(200)
                        .body("disclaimer",equalTo("This data was produced from the CoinDesk Bitcoin Price Index. BPI value data returned as "+c+"."));
            }

        }

    @Test
    public void price_resource_returns_200_with_errored_currency()
    {
        String[] currencies={"CAD","USD","CNY","NPR","MAD","UZS"};
        for (String c:currencies)
        {

            given().log().uri().when().
                    get(host+"/v1/bpi/historical/close.json?currency={currency}",c).then().log().body().statusCode(200)
                    .body("disclaimer",is(not(equalTo("This data was produced from the CoinDesk Bitcoin Price Index. BPI value data returned as kkk"))));
        }

    }

    private static Properties getProperties()
        {
            //load properties from the config file in test/java/resources
            String resourceName = "config.properties"; // could also be a constant
            //
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Properties prop = new Properties();
            try {
                InputStream resourceStream = loader.getResourceAsStream(resourceName);
                prop.load(resourceStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prop;
        }
    }

