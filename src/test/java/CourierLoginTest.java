import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.client.CourierClient;
import ru.yandex.models.CreateCourierRequest;
import ru.yandex.models.LoginCourierRequest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {

    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check courier authorization")
    public void testCourierLoginSuccess() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        int id = courierClient.courierLogin(loginCourierRequest)
                .then().statusCode(200).assertThat().body("id", notNullValue())
                .extract().body().path("id");
        courierClient.deleteCourier(id);
    }

    @Test
    @DisplayName("Check error message for empty login")
    public void testErrorMessageForEmptyLogin() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        loginCourierRequest.setLogin(null);
        courierClient.courierLogin(loginCourierRequest)
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .log().all();
    }

    @Test
    @DisplayName("Check error message for empty password")
    public void testErrorMessageForEmptyPassword() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        loginCourierRequest.setPassword(null);
        courierClient.courierLogin(loginCourierRequest)
                // поведение отличается от документации. Ожидаю получить 400 и "Недостаточно данных для входа", но тест "повисает" и крашится с 504.
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .log().all();
    }

    @Test
    @DisplayName("Check error message for non-existent courier")
    public void testErrorMessageForNonExistentCourier() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        courierClient.courierLogin(loginCourierRequest)
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }

    @Test
    @DisplayName("Check error message for incorrect password")
    public void testErrorMessageForIncorrectPassword() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        loginCourierRequest.setPassword(RandomStringUtils.randomAlphanumeric(8));
        courierClient.courierLogin(loginCourierRequest)
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }

    @Test
    @DisplayName("Check error message for incorrect login")
    public void testErrorMessageForIncorrectLogin() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        loginCourierRequest.setLogin(RandomStringUtils.randomAlphanumeric(8));
        courierClient.courierLogin(loginCourierRequest)
                .then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"))
                .log().all();
    }
}