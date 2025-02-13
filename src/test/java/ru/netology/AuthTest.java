package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.userRegistration.userAnonymous;
import static ru.netology.DataGenerator.userRegistration.userRegistered;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void testLoginRegisteredUserActive() { //успешный вход
        var userReg = userRegistered("active");
        $("[data-test-id='login'] input").setValue(userReg.getLogin());
        $("[data-test-id='password'] input").setValue(userReg.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    void testErrorLoginNotRegisteredUser() { //ошибка входа незарегистрированного пользователя
        var userNotReg = userAnonymous("active");
        $("[data-test-id='login'] input").setValue(userNotReg.getLogin());
        $("[data-test-id='password'] input").setValue(userNotReg.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe((Condition.visible));
    }

    @Test
    void testErrorLoginBlockedUser() {  //ошибка входа заблокированного пользователя
        var userBlock = userRegistered("blocked");
        $("[data-test-id='login'] input").setValue(userBlock.getLogin());
        $("[data-test-id='password'] input").setValue(userBlock.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15))
                .shouldBe((Condition.visible));
    }

    @Test
    void testErrorInvalidLogin() { //ошибка входа с неправильным логином и правильным паролем
        var userReg = userRegistered(("active"));
        $("[data-test-id='login'] input").setValue(DataGenerator.userLogin());
        $("[data-test-id='password'] input").setValue(userReg.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe((Condition.visible));

    }

    @Test
    void testErrorInvalidPassword() {   //ошибка входа с логином зарегистрированного пользователя и неправильным паролем
        var userReg = userRegistered(("active"));
        $("[data-test-id='login'] input").setValue(userReg.getLogin());
        $("[data-test-id='password'] input").setValue(DataGenerator.userPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe((Condition.visible));
    }

}
