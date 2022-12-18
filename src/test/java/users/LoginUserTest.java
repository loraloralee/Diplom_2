package users;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import user.Credentials;
import user.User;
import user.UserClient;
import user.UserGenerator;


public class LoginUserTest {
    private UserClient userClient=new UserClient();
    private  User user= new User();
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();

    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginWithCreatedUserTest(){
        userClient.create(user);
        ValidatableResponse responseLogin= userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        Assert.assertEquals("User unauthorized", 200, statusCode);
        accessToken = responseLogin.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithInvalidEmailPasswordTest(){
        ValidatableResponse responseCreate= userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseLogin= userClient.login(Credentials.credentialsWithInvalidEmailPassword(user));
        int statusCode = responseLogin.extract().statusCode();
        Assert.assertEquals("Email or password are incorrect", 401, statusCode);

    }
    @After
    public void tearDown()
    {
        ValidatableResponse response = userClient.deleteUser(accessToken);
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("User not deleted", 202, statusCode);
    }

}
