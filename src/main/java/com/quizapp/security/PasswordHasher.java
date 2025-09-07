package com.quizapp.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }
    public static boolean verify(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
