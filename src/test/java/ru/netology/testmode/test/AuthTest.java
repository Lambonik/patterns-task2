package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $$(".button").find(exactText("Продолжить")).click();
        $(".heading")
                .shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.exist);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

}
