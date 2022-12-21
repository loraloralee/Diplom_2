package user;

public class Credentials {
    private String email;
    private String password;
    private User user;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Credentials from(User user) {
        return new Credentials(user.getEmail(), user.getPassword());
    }


    public static Credentials credentialsWithInvalidEmailPassword(User user) {
        return new Credentials("25" + user.getEmail(), user.getPassword());
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
