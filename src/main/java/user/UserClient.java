package user;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{
    private static final String USER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String CHANGE_PATH ="/api/auth/user";
    public static final String DELETE_PATH = "api/auth/user";

    public ValidatableResponse create(User user){
        return given()
                .spec(getSpec())
                .log().all()
                .body(user)
                .post(USER_PATH)
                .then()
                .log().all();
    }


    public ValidatableResponse login(Credentials credentials){
        return given()
                .spec(getSpec())
                .log().all()
                .body(credentials)
                .post(LOGIN_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse updateUserWithAuthorization(User user, String accessToken){
        return given()
                .spec(getSpec())
                .log().all()
                .header("Authorization", accessToken)
                .body(user)
                .patch (CHANGE_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse updateUserWithoutAuthorization(User user){
        return given()
                .spec(getSpec())
                .log().all()
                .body(user)
                .patch (CHANGE_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .log().all()
                .delete(DELETE_PATH)
                .then();
    }


}
