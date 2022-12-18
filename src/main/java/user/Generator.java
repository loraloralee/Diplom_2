package user;

public class Generator {
    public static String generateNewEmail() {
        String[] randomArr = {"d", "s", "m", "c", "e", "o", "k", "n", "l", "i", "v"};
        String randomStr = "";
        for (int i = 0; i < 6; i++) {
            String one = randomArr[(int) (Math.random() * 10)];
            randomStr = randomStr + one;
        }

        return randomStr;

    }
}
