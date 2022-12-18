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

public class DataChangeUserTest {


    private static String accessToken;
    private static String email;
    private static String name;
    private UserClient userClient=new UserClient();
    private User user=new User();




    @Before
    public void setUp() {
        userClient = new UserClient();
        user= UserGenerator.getDefault();

    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией")
    public void updateUserDataWithAuth() {
        ValidatableResponse responseCreate = userClient.create(user);
        Credentials credentials  = new Credentials(user.getEmail(), user.getPassword());
        ValidatableResponse responseLogin = userClient.login(credentials);
        accessToken = responseCreate.extract().path("accessToken");
        String userUpdateEmail = "new"+user.getEmail() ;
        String userUpdateName = user.getName() + "new";
        user.setEmail(userUpdateEmail);
        user.setName(userUpdateName);
        ValidatableResponse responseUpdate = userClient.updateUserWithAuthorization(user, accessToken);
        int statusCode = responseUpdate.extract().statusCode();
        Assert.assertEquals("User not updated", 200, statusCode);
        String actualEmail = responseUpdate.extract().path("user.email");
        Assert.assertEquals("E-mail failed", userUpdateEmail, actualEmail);
        String actualName = responseUpdate.extract().path("user.name");
        Assert.assertEquals("Name failed", userUpdateName, actualName);
    }

    @Test
    @DisplayName("Изменение данных пользователя: без авторизации")
    public void updateUserDataWithoutAuth() {
        ValidatableResponse responseCreate = userClient.create(user);
        Credentials credentials  = new Credentials(user.getEmail(), user.getPassword());
        ValidatableResponse responseLogin = userClient.login(credentials);
        accessToken = responseCreate.extract().path("accessToken");
        String userUpdateEmail = "new2"+user.getEmail() ;
        String userUpdateName = user.getName() + "new2";
        user.setEmail(userUpdateEmail);
        user.setName(userUpdateName);
        ValidatableResponse responseUpdate = userClient.updateUserWithoutAuthorization(user);
        int statusCode = responseUpdate.extract().statusCode();
        Assert.assertEquals("You should be authorised", 401, statusCode);
        String actualEmail = responseUpdate.extract().path("user.email");
        Assert.assertNotEquals("E-mail в ответе сервера не совпадает актуальным", userUpdateEmail, actualEmail);
        String actualName = responseUpdate.extract().path("user.name");
        Assert.assertNotEquals("E-mail в ответе сервера не совпадает актуальным", userUpdateName, actualName);
    }
    @After
    public void tearDown()
    {
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse response = userClient.deleteUser(accessToken);
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("User not deleted", 202, statusCode);
    }
}
