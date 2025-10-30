package model;

public class Session {
    private static User loggedUser;

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static boolean isLogged() {
        return loggedUser != null;
    }

    public static void logout() {
        loggedUser = null;
    }
}
