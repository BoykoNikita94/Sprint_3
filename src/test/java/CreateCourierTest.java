import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.client.CourierClient;
import ru.yandex.models.CreateCourierRequest;
import ru.yandex.models.LoginCourierRequest;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    CourierClient courierClient = new CourierClient();

    @Test
    @DisplayName("Check courier creation")
    public void testCreateCourierSuccess() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest)
                .then().statusCode(201)
                .log().all();
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        int id = courierClient.courierLogin(loginCourierRequest)
                .then().extract().body().path("id");
        courierClient.deleteCourier(id);
    }

    @Test
    @DisplayName("Check courier creation. Response contains 'true'")
    public void testResponseTrueForCreateCourierSuccess() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest)
                .then().assertThat().body("ok", equalTo(true))
                .log().all();
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(createCourierRequest.getLogin(), createCourierRequest.getPassword());
        int id = courierClient.courierLogin(loginCourierRequest)
                .then().extract().body().path("id");
        courierClient.deleteCourier(id);
    }

    @Test
    @DisplayName("Error creating identical couriers")
    public void testErrorMessageCreatingIdenticalCouriers() {
        CreateCourierRequest createCourierRequest = CreateCourierRequest.create();
        courierClient.createCourier(createCourierRequest);
        courierClient.createCourier(createCourierRequest)
                //тест упадет, т.к. в соответствии с документацией в message должно передаваться "Этот логин уже используется", а фактически получаем "Этот логин уже используется. Попробуйте другой."
                .then().statusCode(409).assertThat().body("message", equalTo("Этот логин уже используется"))
                .log().all();
    }

    @Test
    @DisplayName("Error creating courier without login")
    public void testErrorMessageForEmptyLogin() {
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(null, password, firstName);
        courierClient.createCourier(createCourierRequest)
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .log().all();
    }

    @Test
    @DisplayName("Error creating courier without password")
    public void testErrorMessageForEmptyPassword() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(login, null, firstName);
        courierClient.createCourier(createCourierRequest)
                .then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .log().all();
    }
}