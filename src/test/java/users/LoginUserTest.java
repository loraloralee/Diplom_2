package users;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.apache.http.HttpStatus.*;

import user.Credentials;
import user.User;
import user.UserClient;
import user.UserGenerator;


public class LoginUserTest {
    private UserClient userClient = new UserClient();
    private User user = new User();
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @After
    public void tearDown() {
        ValidatableResponse response = userClient.deleteUser(accessToken);
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("User not deleted", SC_ACCEPTED, statusCode);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginWithCreatedUserTest() {
        userClient.createUser(user);
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        Assert.assertEquals("User unauthorized", SC_OK, statusCode);
        accessToken = responseLogin.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithInvalidEmailPasswordTest() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseLogin = userClient.login(Credentials.credentialsWithInvalidEmailPassword(user));
        int statusCode = responseLogin.extract().statusCode();
        Assert.assertEquals("Email or password are incorrect", SC_UNAUTHORIZED, statusCode);

    }

}
