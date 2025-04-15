package uta.cse3310.DB;

import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordManager 
{
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    //salt must be store with each user profile, otherwise decoding and checking will be impossible
    public static byte[] generateSalt()
    { //will need to generate a new salt for every new (or changed) password, unique to each user

        //creates a random that will generate a random number of 128 bits
        SecureRandom saltRandom = new SecureRandom();
        byte[] salt = new byte[16];  //creates empty salt
        saltRandom.nextBytes(salt);  //fills salt randomly with the SecureRandom
    
        return salt;
    }
    // Placeholder for password hashing
    public static String hashPassword(String password, byte[] salt) 
    { 
        try {
            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    // Placeholder for password verification
    public static boolean verifyPassword(String password, String storedHash, byte[] salt) {
        String newHash = hashPassword(password, salt);
        return newHash.equals(storedHash);
    }

}
