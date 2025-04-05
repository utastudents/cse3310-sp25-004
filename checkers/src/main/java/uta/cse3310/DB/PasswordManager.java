package uta.cse3310.DB;

public class PasswordManager {
    
    /* TODO: salt generation implementation */
    public static String generateSalt() {
        return "";
    }

    /* TODO :  password hashing */
    public static String hashPassword(String password, String salt) {
        return "";
    }

    /* TODO : password verification code using salt with stored hash */
    public static boolean verifyPassword(String password, String storedHash, String salt) {
        return false; 
    }
}
