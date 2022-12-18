package users;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import user.UserClient;
import user.User;
import user.UserGenerator;

public class CreateUserTest {
    private UserClient userClient=new UserClient();
    private  User user= new User();
    private User doubleUser;
    private User emptyFieldUser;
    private String accessToken;

    @Before
    public void setUp(){
        userClient=new UserClient();
        user= UserGenerator.getDefault();
        doubleUser=UserGenerator.getDoubleUser();
        emptyFieldUser=UserGenerator.getEmptyFieldUser();

    }
    @Test
    @DisplayName("Создать уникального пользователя")
    public void userCanBeCreatedTest(){
        ValidatableResponse responseCreate= userClient.create(user);
        int statusCode = responseCreate.extract().statusCode();
        Assert.assertEquals("User uncreated", 200, statusCode);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void userWithDoubleUserTest(){
        ValidatableResponse responseCreate= userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseCreateDoubleUser= userClient.create(doubleUser);
        int statusCode = responseCreateDoubleUser.extract().statusCode();
        Assert.assertEquals("User already exists", 403, statusCode);

    }
    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    public void userWithEmptyFieldTest(){
        ValidatableResponse responseCreate= userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");
        accessToken = accessToken.split("Bearer ")[1];
        ValidatableResponse responseCreateWithEmptyFieldUser= userClient.create(emptyFieldUser);
        int statusCode = responseCreateWithEmptyFieldUser.extract().statusCode();
        Assert.assertEquals("Email, password and name are required fields", 403, statusCode);

    }
    @After
    public void tearDown()
    {
        ValidatableResponse response = userClient.deleteUser(accessToken);
        int statusCode = response.extract().statusCode();
        Assert.assertEquals("User not deleted", 202, statusCode);
    }

}
