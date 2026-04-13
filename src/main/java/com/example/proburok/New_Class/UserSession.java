package com.example.proburok.New_Class;

import com.example.proburok.job_User.User;

public class UserSession {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getUchastok() {
        if (currentUser != null && currentUser.getLocation().equals("Проходчик")) {
            return "Участок КПУ ГПР №1 ";
        }
//        if (currentUser != null && currentUser.getLocation().equals("Восточный")) {
//            return "Восточный участок";
//        }
        return null;
    }
}