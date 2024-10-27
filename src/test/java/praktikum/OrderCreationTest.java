package praktikum;

import java.util.List;

import org.junit.Test;
import org.junit.Before;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import praktikum.api.ClientOrder;
import praktikum.api.ClientUser;
import praktikum.example.Order;
import praktikum.example.User;
import praktikum.services.UserGenerator;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Create order")
public class OrderCreationTest {
    private ValidatableResponse response;
    private User user;
    private Order order;
    private ClientUser userClient;
    private ClientOrder orderClient;

    @Before
    @Step("Подготовка пользователя и заказа")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        order = new Order();
        userClient = new ClientUser();
        orderClient = new ClientOrder();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией для пользователя {user.email}")
    @Step("Создание заказа с авторизацией для пользователя {user.email}")
    public void orderCreateByAuthorization() {
        fillListIngredients();
        createUser();
        String accessToken = loginUser();
        createOrderByAuthorization(accessToken);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Step("Создание заказа без авторизации")
    public void orderCreateWithoutAuthorization() {
        fillListIngredients();
        createOrderWithoutAuthorization();
    }

    @Test
    @DisplayName("Создание заказа без авторизации и ингредиентов")
    @Step("Создание заказа без авторизации и ингредиентов")
    public void orderCreateWithoutAuthorizationAndIngredients() {
        response = orderClient.createOrderWithoutAuthorization(order);
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Step("Создание заказа с неверным хешем ингредиента")
    public void orderCreateWithInvalidIngredientHash() {
        // Реализуйте тест для создания заказа с неверным хешем ингредиента.
    }

    @Step("Заполнение списка ингредиентов")
    private void fillListIngredients() {
        response = orderClient.getAllIngredients();
        List<String> list = response.extract().path("data._id");
        order.setIngredients(list.subList(0, 2)); // Заполнение случайными ингредиентами
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

    @Step("Создание заказа с авторизацией")
    private void createOrderByAuthorization(String accessToken) {
        response = orderClient.createOrderByAuthorization(order, accessToken);
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_OK));
    }

    @Step("Создание заказа без авторизации")
    private void createOrderWithoutAuthorization() {
        response = orderClient.createOrderWithoutAuthorization(order);
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_OK));
    }
}
