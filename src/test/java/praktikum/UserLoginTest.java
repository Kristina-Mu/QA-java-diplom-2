package praktikum;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.ValidatableResponse;
import praktikum.api.ClientUser;
import praktikum.example.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("User Login")
public class UserLoginTest {
    private ClientUser clientUser;
    private String existingUserEmail = "existingUser@mail.ru";
    private String validPassword = "password";
    private String userName = "User";

    @Before
    @Step("Подготовка существующего пользователя")
    public void setup() {
        clientUser = new ClientUser();
        User newUser = new User(existingUserEmail, validPassword, userName);
        clientUser.createUser(newUser); // Создание пользователя через API
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Step("Логин под существующим пользователем")
    public void loginExistingUser() {
        User user = new User(existingUserEmail, validPassword, userName);
        ValidatableResponse response = clientUser.loginUser(user, validPassword);
        assertThat("Expected status code <200> but was <" + response.extract().statusCode() + ">", response.extract().statusCode(), equalTo(200));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Step("Логин с неверным логином и паролем")
    public void loginWithInvalidCredentials() {
        User user = new User("invalid@mail.ru", "wrongPassword", "User");
        ValidatableResponse response = clientUser.loginUser(user, "invalidPassword");
        assertThat("Expected status code <401> but was <" + response.extract().statusCode() + ">", response.extract().statusCode(), equalTo(401));
    }
}
