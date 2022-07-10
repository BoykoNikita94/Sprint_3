import Models.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Date;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String[] colors;

    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Check order creation. Response contains 'track'")
    public void createCourier() {
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        String lastName = RandomStringUtils.randomAlphanumeric(8);
        String address = RandomStringUtils.randomAlphanumeric(8);
        int metroStation = new Random().nextInt(8);
        String phone = RandomStringUtils.randomAlphanumeric(8);
        int rentTime = new Random().nextInt(8);
        String deliveryDate = new Date().toString();
        String comment = RandomStringUtils.randomAlphanumeric(8);

        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then().statusCode(201).assertThat().body("track", notNullValue())
                .log().all();
    }
}
