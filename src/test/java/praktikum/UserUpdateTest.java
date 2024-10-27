package praktikum;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import praktikum.api.ClientUser;
import praktikum.example.User;
import praktikum.services.UserGenerator;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Update user")
public class UserUpdateTest {
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
    private ValidatableResponse response;
    private ClientUser userClient;
    private User user;
    private String accessToken;

    @Before
    @Step("Создание случайного пользователя")
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new ClientUser();
    }

    @After
    @Step("Очистка состояния пользовательского аккаунта")
    public void clearState() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Обновление пользователя по авторизации")
    @Step("Обновление пользователя по авторизации")
    public void updateUserByAuthorization() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        response = userClient.updateUserByAuthorization(UserGenerator.getRandomUser(), accessToken);

        int statusCode = response.extract().statusCode();
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("User is update incorrect", isUpdate, equalTo(true));
    }

    @Test
    @DisplayName("Обновление пользователя без авторизации")
    @Step("Обновление пользователя без авторизации")
    public void updateUserWithoutAuthorization() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.updateUserWithoutAuthorization(UserGenerator.getRandomUser());

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("User is updated correct", isUpdate, equalTo(false));
    }
}
