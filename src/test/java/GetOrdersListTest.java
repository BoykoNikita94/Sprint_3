import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.client.OrdersClient;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersListTest {

    OrdersClient ordersClient = new OrdersClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check response contains list of orders")
    public void testGetOrderListSuccess() {
        ordersClient.getOrderList()
                .then().assertThat().body("orders", notNullValue())
                .log().all();
    }
}