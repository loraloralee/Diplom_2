package orders;

import io.restassured.response.ValidatableResponse;
import order.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import user.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {
    //private static final String MESSAGE_BAD_REQUEST = "Ingredient ids must be provided";
    private ValidatableResponse response;
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        user = UserGenerator.getDefault();
        order = new Order();
        userClient = new UserClient();
        orderClient = new OrderClient();

    }
    public void createListIngredients() {
        response = orderClient.getAllIngredients();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(1));
        ingredients.add(list.get(0));
    }
    public void createListInvalidIngredients() {
        response = orderClient.getAllIngredients();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add("invalid hash ingredient");
        ingredients.add(list.get(1));
        ingredients.add(list.get(2));
    }

    @Test
    public void orderCreateByAuthorizationWithIngredientsTest() {
        createListIngredients();
        response = userClient.create(user);
        Credentials credentials = new Credentials(user.getEmail(), user.getPassword());
        response = userClient.login(Credentials.from(user));
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderByAuthorization(order, accessToken);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        String orderId = response.extract().path("order._id");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(200));
        assertThat("Order is create incorrect", isCreate, equalTo(true));
        assertThat("Order number is null", orderNumber, notNullValue());
        assertThat("Order id is null", orderId, notNullValue());
    }


    @Test
    public void orderCreateWithoutAuthorizationWithIngredientsTest() {
        createListIngredients();
        response = userClient.create(user);
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        String orderId = response.extract().path("order._id");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(200));
        assertThat("Order is create incorrect", isCreate, equalTo(true));
        assertThat("Order number is null", orderNumber, notNullValue());
        assertThat("Order id is null", orderId, nullValue());
    }
    @Test
    public void orderCreateWithAuthorizationWithoutIngredientsTest() {
        response = userClient.create(user);
        Credentials credentials = new Credentials(user.getEmail(), user.getPassword());
        response = userClient.login(Credentials.from(user));
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderByAuthorization(order, accessToken);
        int statusCode = response.extract().statusCode();
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Ingredient ids must be provided", statusCode, equalTo(400));


    }
    @Test
    public void orderCreateWithoutAuthorizationWithoutIngredientsTest() {
        response = userClient.create(user);
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Ingredient ids must be provided", statusCode, equalTo(400));
    }

    @Test
    public void orderCreateByAuthorizationWithInvalidIngredientsTest() {
        createListInvalidIngredients();
        response = userClient.create(user);
        Credentials credentials = new Credentials(user.getEmail(), user.getPassword());
        response = userClient.login(Credentials.from(user));
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderByAuthorization(order, accessToken);
        int statusCode = response.extract().statusCode();
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(500));

    }

    @Test
    public void getOrderByAuthorizationTest() {
        response = userClient.create(user);
        Credentials credentials = new Credentials(user.getEmail(), user.getPassword());
        response = userClient.login(Credentials.from(user));
        String accessToken = response.extract().path("accessToken");
        response= orderClient.getAllOrders();
        response=orderClient.getOrdersByAuthorization(accessToken);
        int statusCode = response.extract().statusCode();
        List <String> orders=response.extract().path("orders");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(200));


    }
    @Test
    public void getOrderWithoutAuthorizationTest() {
        response = userClient.create(user);
        String accessToken = response.extract().path("accessToken");
        response= orderClient.getAllOrders();
        response=orderClient.getOrdersWithoutAuthorization();
        int statusCode = response.extract().statusCode();
        List <String> orders=response.extract().path("orders");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(401));


    }

}
