package user;

public class UserGenerator {
    public static User getDefault(){
        return new User(Generator.generateNewEmail()+"@mail.ru","password_1","name_1");
    }

    public static User getDoubleUser(){
        return new User("User_2@mail.ru","Password_2","Name_2");
    }

    public static User getEmptyFieldUser(){
        return new User("","Password_3","Name_3");
    }

}
