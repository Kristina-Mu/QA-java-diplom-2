package praktikum;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import praktikum.api.ClientUser;
import praktikum.example.User;
import praktikum.services.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Create user")
public class UserCreationTest {
    private static final String MESSAGE_FORBIDDEN = "User already exists";
    private static final String MESSAGE_FORBIDDEN_EMPTY_FIELD = "Email, password and name are required fields";
    private ValidatableResponse response;
    private ClientUser userClient;
    private User user;

    @Before
    @Step("Подготовка случайного пользователя")
    public void setUp() {
        userClient = new ClientUser();
        user = UserGenerator.getRandomUser(); // Готовим случайного пользователя
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Step("Создать уникального пользователя")
    public void createUniqueUser() {
        response = userClient.createUser(user);
        assertThat("Code not equal", response.extract().statusCode(), equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Step("Создать пользователя, который уже зарегистрирован")
    public void createExistingUser() {
        // Сначала создаем пользователя
        userClient.createUser(user);
        // Попробуем снова зарегистрировать того же пользователя
        response = userClient.createUser(user);
        response.statusCode(403);
        response.body("message", equalTo(MESSAGE_FORBIDDEN));
    }

    @Test
    @DisplayName("Создать пользователя без обязательного поля")
    @Step("Создать пользователя без обязательного поля")
    public void createUserWithoutRequiredField() {
        User userWithoutEmail = new User(null, "password", "User without email");
        response = userClient.createUser(userWithoutEmail);
        response.statusCode(403);
        response.body("message", equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
    }
}
