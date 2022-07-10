import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;


public class GetOrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check response contains list of orders")
    public void test() {
        given()
                .header("Content-Type", "Application/json")
                .get("/api/v1/orders")
                .then().assertThat().body("orders", notNullValue())
                .log().all();
    }
}