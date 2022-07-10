import Models.CreateCourierRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private void deleteCourier(CreateCourierRequest data) {
        int id = given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
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
    @DisplayName("Check courier creation")
    public void createCourier() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest data = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .log().all();
        deleteCourier(data);
    }

    @Test
    @DisplayName("Check courier creation. Response contains 'true'")
    public void checkResponse() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest data = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("ok", equalTo(true))
                .log().all();
        deleteCourier(data);
    }

    @Test
    @DisplayName("Error creating identical couriers")
    public void createIdenticalCouriers() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest data = new CreateCourierRequest(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier")
                //тест упадет, т.к. в соответствии с документацией в message должно передаваться "Этот логин уже используется", а фактически получаем "Этот логин уже используется. Попробуйте другой."
                .then().statusCode(409).assertThat().body("message", equalTo("Этот логин уже используется"))
                .log().all();
    }

    @Test
    @DisplayName("Error creating courier without login")
    public void createCourierWithoutLogin() {
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest data = new CreateCourierRequest(null, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .log().all();
    }

    @Test
    @DisplayName("Error creating courier without password")
    public void createCourierWithoutPassword() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest data = new CreateCourierRequest(login, null, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(data)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .log().all();
    }
}