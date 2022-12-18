package order;

import io.restassured.response.ValidatableResponse;
import user.Client;
import user.User;


import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    String accessToken;
   private static final String ORDER_CREATE_PATH="/api/orders";
    private static final String GET_INGREDIENTS_PATH="/api/ingredients";
    public static final String GET_ALL_ORDERS = "api/orders/all";






    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getSpec())
                .log().all()
                .get(GET_INGREDIENTS_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(ORDER_CREATE_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given()
                .spec(getSpec())
                .log().all()
                .get(ORDER_CREATE_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getSpec())
                .log().all()
                .get(GET_ALL_ORDERS)
                .then()
                .log().all();
    }

    public ValidatableResponse createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(ORDER_CREATE_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .log().all()
                .post(ORDER_CREATE_PATH)
                .then()
                .log().all();
    }
}

