import Models.CreateCourierRequest;
import Models.LoginCourierRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {

    private void deleteCourier(int id) {
        given()
                .header("Content-type", "application/json")
                .and()
                .delete("/api/v1/courier/" + id)
                .then().statusCode(200);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check courier authorization")
    public void courierLogin() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, password);
        int id = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                .then().statusCode(200).assertThat().body("id", notNullValue())
                .extract().body().path("id");
        deleteCourier(id);
    }

    @Test
    @DisplayName("Error authorize courier without login")
    public void courierLoginWithoutLogin() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(null, password);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .log().all();
    }

    @Test
    @DisplayName("Error authorize courier without password")
    public void courierLoginWithoutPassword() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, null);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                // поведение отличается от документации. Ожидаю получить 400 и "Недостаточно данных для входа", но тест "повисает" и крашится с 504.
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .log().all();
    }

    @Test
    @DisplayName("Error authorization by non-existent courier")
    public void loginWithNonExistentCourier() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, password);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }

    @Test
    @DisplayName("Error authorize courier with wrong password")
    public void courierLoginWithWrongPassword() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, RandomStringUtils.randomAlphanumeric(8));
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }

    @Test
    @DisplayName("Error authorize courier with wrong login")
    public void courierLoginWithWrongLogin() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier");
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(RandomStringUtils.randomAlphanumeric(8), password);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierRequest)
                .post("/api/v1/courier/login")
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }
}