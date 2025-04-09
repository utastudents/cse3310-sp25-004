package uta.cse3310.DB;
import java.security.SecureRandom;

public class PasswordManager {
    public static byte[] generateSalt() { //will need to generate a new salt for every new (or changed) password, unique to each user

        //creates a random that will generate a random number of 128 bits
        SecureRandom saltRandom = new SecureRandom();
        byte[] salt = new byte[16];  //creates empty salt
        saltRandom.nextBytes(salt);  //fills salt randomly with the SecureRandom
    
        return salt;
    }
    //salt must be store with each user profile, otherwise decoding and checking will be impossible

    // Placeholder for password hashing
    public static String hashPassword(String password, byte[] salt) { //will be used to encode passwords




        return ""; // Placeholder
    }

    // Placeholder for password verification
    public static boolean verifyPassword(String password, String storedHash, byte[] salt) { // will be used to (temporarily) decode passwwords and check if it matches user input

        


        return false; // Placeholder
    }
}
