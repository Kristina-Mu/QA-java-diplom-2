package praktikum;

import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import praktikum.api.ClientOrder;
import praktikum.api.ClientUser;
import praktikum.example.User;
import praktikum.services.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderRetrievalTest {
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
    private ValidatableResponse response;
    private User user;
    private ClientUser userClient;
    private ClientOrder orderClient;

    @Before
    @Step("Подготовка пользователя")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new ClientUser();
        orderClient = new ClientOrder();
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    @Step("Получение заказов авторизованным пользователем")
    public void getOrdersForAuthorizedUser() {
        createUser();
        String accessToken = loginUser();
        getOrdersByAuthorization(accessToken);
    }

    @Test
    @DisplayName("Получение заказов неавторизованным пользователем")
    @Step("Получение заказов неавторизованным пользователем")
    public void getOrdersForUnauthorizedUser() {
        getOrdersWithoutAuthorization();
    }

    @Step("Создать пользователя")
    private void createUser() {
        response = userClient.createUser(user);
    }

    @Step("Логин пользователя")
    private String loginUser() {
        response = userClient.loginUser(user, response.extract().path("accessToken"));
        return response.extract().path("accessToken");
    }

    @Step("Получение заказов по авторизации")
    private void getOrdersByAuthorization(String accessToken) {
        response = orderClient.getOrdersByAuthorization(accessToken);
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_OK));
    }

    @Step("Получение заказов без авторизации")
    private void getOrdersWithoutAuthorization() {
        response = orderClient.getOrdersWithoutAuthorization();
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_UNAUTHORIZED));
    }
}
