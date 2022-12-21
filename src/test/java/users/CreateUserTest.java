package users;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.apache.http.HttpStatus.*;

import user.UserClient;
import user.User;
import user.UserGenerator;


public class CreateUserTest {
    private UserClient userClient = new UserClient();
    private User user = new User();
    private User doubleUser;
    private User emptyFieldUser;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        doubleUser = UserGenerator.getDoubleUser();
        emptyFieldUser = UserGenerator.getEmptyFieldUser();

    }

    @After
    public void tearDown() {
        ValidatableResponse response = userClient.deleteUser(accessToken);
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("User not deleted", SC_ACCEPTED, statusCode);
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    public void userCanBeCreatedTest() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        int statusCode = responseCreate.extract().statusCode();
        Assert.assertEquals("User uncreated", SC_OK, statusCode);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void userWithDoubleUserTest() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseCreateDoubleUser = userClient.createUser(doubleUser);
        int statusCode = responseCreateDoubleUser.extract().statusCode();
        Assert.assertEquals("User already exists", SC_FORBIDDEN, statusCode);

    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    public void userWithEmptyFieldTest() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseCreateWithEmptyFieldUser = userClient.createUser(emptyFieldUser);
        int statusCode = responseCreateWithEmptyFieldUser.extract().statusCode();
        Assert.assertEquals("Email, password and name are required fields", SC_FORBIDDEN, statusCode);

    }

}
