package main.java.bankManagementSystem;


import org.mindrot.jbcrypt.BCrypt;

public class WelcomeScreen {
    public static void main(String[] args) {
        String password = "hellothere";
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
    }
}
