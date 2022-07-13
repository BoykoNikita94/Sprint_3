package ru.yandex.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.models.CreateCourierRequest;
import ru.yandex.models.LoginCourierRequest;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Create courier")
    public Response createCourier(CreateCourierRequest createCourierRequest) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Courier login")
    public Response courierLogin(LoginCourierRequest loginCourierRequest) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login");
    }

    @Step("Delete courier")
    public Response deleteCourier(int id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .delete("/api/v1/courier/" + id);
    }
}