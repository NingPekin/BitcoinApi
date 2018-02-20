package coindeskAPI;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class RealTimePriceAPI {

    private static String host;

    @BeforeClass
    static public  void setup()
    {
        Properties prop = getProperties();
        host = prop.getProperty("coindesk.url");
    }

    @Test
    public void
    price_resource_returns_200_with_expected_price()
    {
        HashMap<String, String> currencyPair = new HashMap<>();
        currencyPair.put("USD", "United States Dollar");
        currencyPair.put("CNY", "Chinese Yuan");
        for (String key : currencyPair.keySet())
        {
            given().log().uri().
                    when().
                    get(host + "/v1/bpi/currentprice/{currency}.json",
                            key).
                    then().log().body().
                    statusCode(200).
                    body("bpi."+key+".description", equalTo(currencyPair.get(key)));
        }

    }

    @Test
    public void
    price_resource_returns_200_with_errored_price()
    {
        HashMap<String, String> currencyPair = new HashMap<>();
        currencyPair.put("USD", "United States Dollar");
        currencyPair.put("CNY", "Chinese Yuan");
        for (String key : currencyPair.keySet())
        {
            given().log().uri().
                    when().
                    get(host + "/v1/bpi/currentprice/{currency}.json",
                            key).
                    then().log().body().
                    statusCode(200).
                    body("bpi."+key+".description", is(not(equalTo("usa"))));
        }

    }


    private static Properties getProperties() {
        //load properties from the config file
        String resourceName = "config.properties"; // could also be a constant
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
