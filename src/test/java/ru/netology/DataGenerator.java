
package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    static void setUpAll(RegistrationDto user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String userLogin() {
        return faker.name().username();
    }

    public static String userPassword() {
        return faker.internet().password();
    }

    public static class userRegistration {
        private userRegistration() {
        }

        public static RegistrationDto userAnonymous(String status) {
            return new RegistrationDto(userLogin(), userPassword(), status);
        }

        public static RegistrationDto userRegistered(String status) {
            val userRegistered = userAnonymous(status);
            setUpAll(userRegistered);
            return userRegistered;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
