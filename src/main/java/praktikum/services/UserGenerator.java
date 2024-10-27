package praktikum.services;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.RandomStringUtils;
import praktikum.example.User;

public class UserGenerator { //
    public static User getRandomUser() {
        String name = RandomStringUtils.randomAlphabetic(7);
        String email = name.toLowerCase() + "@mail.ru";
        String password = RandomStringUtils.randomAlphabetic(7);

        Allure.addAttachment("Email : ", email);
        Allure.addAttachment("Password : ", password);
        Allure.addAttachment("Name : ", name);

        return new User(email, password, name);
    }
}