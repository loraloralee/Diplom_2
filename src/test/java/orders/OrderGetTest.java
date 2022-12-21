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
import static org.apache.http.HttpStatus.*;

public class OrderGetTest {
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

    @Test
    public void getOrderByAuthorizationTest() {
        createListIngredients();
        response = userClient.createUser(user);
        Credentials credentials = new Credentials(user.getEmail(), user.getPassword());
        response = userClient.login(Credentials.from(user));
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderByAuthorization(order, accessToken);
        response = orderClient.getOrdersByAuthorization(accessToken);
        int statusCode = response.extract().statusCode();
        List<String> orders = response.extract().path("orders");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Orders  is null", orders, notNullValue());


    }

    @Test
    public void getOrderWithoutAuthorizationTest() {
        createListIngredients();
        response = userClient.createUser(user);
        String accessToken = response.extract().path("accessToken");
        response = orderClient.createOrderByAuthorization(order, accessToken);
        response = orderClient.getOrdersWithoutAuthorization();
        int statusCode = response.extract().statusCode();
        List<String> orders = response.extract().path("orders");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Orders  is not null", orders, nullValue());
    }

}
