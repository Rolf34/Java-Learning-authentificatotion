import java.util.Scanner;
import javax.naming.AuthenticationException;

public class App {
    private static final int MAX_USERS = 15;
    private static final int MIN_USERNAME_LENGTH = 5;
    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final int MIN_DIGITS_IN_PASSWORD = 3;
    private static final int MIN_SPECIAL_CHARS_IN_PASSWORD = 1;
    
    private String[] usernames;
    private String[] passwords;
    private String[] bannedPasswords;
    private int userCount;
    private int bannedPasswordsCount;
    private static Scanner scanner = new Scanner(System.in);

    public App() {
        usernames = new String[MAX_USERS];
        passwords = new String[MAX_USERS];
        bannedPasswords = new String[50];

        bannedPasswords[0] = "password";
        bannedPasswords[1] = "admin";
        bannedPasswords[2] = "pass";
        bannedPasswords[3] = "qwerty";
        bannedPasswords[4] = "ytrewq"; 
        bannedPasswords[5] = "123456";
        bannedPasswordsCount = 6;
        userCount = 0;
    }

    public void register(String username, String password) throws AuthenticationException {
        if (userCount >= MAX_USERS) {
            throw new AuthenticationException("Max users reached(15)");
        }
        trueUsername(username);

        for(int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                throw new AuthenticationException("Username already exists");
            }
        } 
        truePassword(password);
        usernames[userCount] = username;
        passwords[userCount] = password;
        userCount++;

        System.out.println("User registered successfully");
    }

    public void delete(String username) throws AuthenticationException {
        int userIndex = -1;
        
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                userIndex = i;
                break;
            }
        }
        
        if (userIndex == -1) {
            throw new AuthenticationException("User with username '" + username + "' not found.");
        }
        
        for (int i = userIndex; i < userCount - 1; i++) {
            usernames[i] = usernames[i + 1];
            passwords[i] = passwords[i + 1];
        }
        
        usernames[userCount - 1] = null;
        passwords[userCount - 1] = null;
        userCount--;
        
        System.out.println("User deleted successfully.");
    }
    
    public void authenticateUser(String username, String password) throws AuthenticationException {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                if (passwords[i].equals(password)) {
                    System.out.println("User authenticated successfully.");
                    return;
                } else {
                    throw new AuthenticationException("Incorrect password.");
                }
            }
        }
        
        throw new AuthenticationException("User with username '" + username + "' not found.");
    }

    private void trueUsername(String username) throws AuthenticationException {
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new AuthenticationException("Username must be at least " + MIN_USERNAME_LENGTH + " characters long.");
        }

        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                throw new AuthenticationException("Username cannot contain spaces.");
            }
        }
    }

    private void truePassword(String password) throws AuthenticationException {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new AuthenticationException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) == ' ') {
                throw new AuthenticationException("Password cannot contain spaces.");
            }
        }

        int digitCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        if (digitCount < MIN_DIGITS_IN_PASSWORD) {
            throw new AuthenticationException("Password must contain at least " + MIN_DIGITS_IN_PASSWORD + " digits.");
        }

        int specialCharCount = 0;
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                specialCharCount++;
            }
        }

        if (specialCharCount < MIN_SPECIAL_CHARS_IN_PASSWORD) {
            throw new AuthenticationException("Password must contain at least " + MIN_SPECIAL_CHARS_IN_PASSWORD + " special character.");
        }

        for (int i = 0; i < bannedPasswordsCount; i++) {
            if (bannedPasswords[i].equals(password)) {
                throw new AuthenticationException("Password is banned.");
            }
        }
    }

    public void addBannedPassword(String bannedPassword) throws AuthenticationException {
        for (int i = 0; i < bannedPasswordsCount; i++) {
            if (bannedPasswords[i].equals(bannedPassword)) {
                throw new AuthenticationException("This password is already in the forbidden list.");
            }
        }

        if (bannedPasswordsCount < bannedPasswords.length) {
            bannedPasswords[bannedPasswordsCount] = bannedPassword;
            bannedPasswordsCount++;
            System.out.println("Password '" + bannedPassword + "' was banned successfully.");
        } else {
            throw new AuthenticationException("Maximum number of forbidden passwords reached.");
        }  
    }

    public void printAllUsers() {
        if (userCount == 0) {
            System.out.println("No users registered.");
            return;
        }
        
        System.out.println("List of users:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + ". " + usernames[i]);
        }
    }

    public static void main(String[] args) {
        App auth = new App();
        boolean running = true;
        
        while (running) {
            try {
                System.out.println("""
                        === Authentication System ===
                        1. Register new user
                        2. Delete user
                        3. Authenticate user
                        4. Add banned password
                        5. Show all users
                        0. Exit""");
                System.out.print("Choose option: ");
                
                int choice = readInt();
                
                switch (choice) {
                    case 1:
                        registerUserMenu(auth);
                        break;
                    case 2:
                        deleteUserMenu(auth);
                        break;
                    case 3:
                        authenticateUserMenu(auth);
                        break;
                    case 4:
                        addBannedPasswordMenu(auth);
                        break;
                    case 5:
                        auth.printAllUsers();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void registerUserMenu(App auth) throws AuthenticationException {
        System.out.print("Enter username (min " + MIN_USERNAME_LENGTH + " characters, no spaces): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password (min " + MIN_PASSWORD_LENGTH + " characters, at least " + 
                MIN_DIGITS_IN_PASSWORD + " digits, at least 1 special character, no spaces): ");
        String password = scanner.nextLine();
        
        auth.register(username, password);
    }
    
    private static void deleteUserMenu(App auth) throws AuthenticationException {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        
        auth.delete(username);
    }
    
    private static void authenticateUserMenu(App auth) throws AuthenticationException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        auth.authenticateUser(username, password);
    }
    
    private static void addBannedPasswordMenu(App auth) throws AuthenticationException {
        System.out.print("Enter password to ban: ");
        String password = scanner.nextLine();
        
        auth.addBannedPassword(password);
    }
    
    private static int readInt() throws AuthenticationException {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new AuthenticationException("Invalid input. Please enter a number.");
        }
    }
}