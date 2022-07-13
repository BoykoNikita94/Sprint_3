package ru.yandex.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.models.Order;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    @Step("Create order")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Get order list")
    public Response getOrderList() {
        return given()
                .header("Content-Type", "Application/json")
                .get("/api/v1/orders");
    }
}