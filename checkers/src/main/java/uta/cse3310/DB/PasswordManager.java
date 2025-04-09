package uta.cse3310.DB;

public class PasswordManager {
    public static byte[] generateSalt() {

        //creates a random that will generate a random number of 128 bits
        SecureRandom saltRandom = new SecureRandom();
        byte[] salt = new byte[16];  //creates empty salt
        saltRandom.nextBytes(salt);  //fills salt randomly with the SecureRandom
    
        return salt;
    }

    // Placeholder for password hashing
    public static String hashPassword(String password, byte[] salt) {




        return ""; // Placeholder
    }

    // Placeholder for password verification
    public static boolean verifyPassword(String password, String storedHash, byte[] salt) {

        


        return false; // Placeholder
    }
}
