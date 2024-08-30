import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class login {

    private static JFrame frame;
    private static JPanel loginPanel;
    private static JPanel accessCodePanel;

    private static String mcUsername;
    private static String skinPath;

    // User data storage
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, User> accessCodes = new HashMap<>();

    public static void main(String[] args) {
        initializeUsers();

        // Initialize the frame
        frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);

        // Create the login panel
        loginPanel = new JPanel(null);
        loginPanel.setBackground(Color.BLACK);
        loginPanel.setBounds(0, 0, 300, 300);

        // Create the access code panel
        accessCodePanel = new JPanel(null);
        accessCodePanel.setBackground(Color.BLACK);
        accessCodePanel.setBounds(0, 0, 300, 300);
        accessCodePanel.setVisible(false);

        // Initialize the login panel components
        initLoginPanel();

        // Initialize the access code panel components
        initAccessCodePanel();

        // Add panels to the frame
        frame.add(loginPanel);
        frame.add(accessCodePanel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void initializeUsers() {
        // Add users and their credentials to the map
        users.put("atom20003113", new User("atom20003113", "0000", "dev1", "1.png"));
        users.put("Glitch_422", new User("Glitch_422", "The_nothing_606", "dev2", "2.png"));
        users.put("fullserverZil", new User("fullserverZil", "Kaan12*/", "dev3", "3.png"));
        users.put("MrDev5", new User("MrDev5", "Welcome1122", "dev5", "5.png"));
        users.put("Developer6", new User("Developer6", "Dev6Al", "dev6", "6.png"));
        users.put("PLAYTESTER", new User("PLAYTESTER", "PLAYTESTER", "[PLAYTESTER]", "PLAYTESTER.png"));

        // Add access codes and their corresponding users to the map
        accessCodes.put("206446-35856292-7272730-2828303-9726-047017", new User("atom20003113", "0000", "dev1", "1.png"));
        accessCodes.put("789505-55263465-1743129-2972267-3174-286293", new User("Glitch_422", "The_nothing_606", "dev2", "2.png"));
        accessCodes.put("647496-45555624-8616548-6169525-5726-359641", new User("fullserverZil", "Kaan12*/", "dev3", "3.png"));
        accessCodes.put("052566-14748653-2266246-5696347-3975-010365", new User("MrDev5", "Welcome1122", "dev5", "5.png"));
        accessCodes.put("829474-86548555-8594861-3339195-3804-377520", new User("Developer6", "Dev6Al", "dev6", "6.png"));
        accessCodes.put("849595-54468634-8626348-6139482-8457-678915", new User("PLAYTESTER", "PLAYTESTER", "[PLAYTESTER]", "PLAYTESTER.png"));
    }

    private static void initLoginPanel() {
        // Title label
        JLabel titleLabel = new JLabel("admin login");
        titleLabel.setBounds(100, 10, 100, 30);
        titleLabel.setForeground(Color.GREEN);
        loginPanel.add(titleLabel);

        // Username label and text field
        JLabel usernameLabel = new JLabel("username:");
        usernameLabel.setBounds(20, 50, 100, 25);
        usernameLabel.setForeground(Color.GREEN);
        loginPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 50, 150, 25);
        loginPanel.add(usernameField);

        // Password label and text field
        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setBounds(20, 90, 100, 25);
        passwordLabel.setForeground(Color.GREEN);
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 90, 150, 25);
        loginPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("login");
        loginButton.setBounds(100, 130, 100, 25);
        loginPanel.add(loginButton);

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setBounds(20, 170, 250, 25);
        errorLabel.setForeground(Color.RED);
        loginPanel.add(errorLabel);

        // 'Don't know your login details?' label
        JLabel dontKnowLabel = new JLabel("don't know your login details?");
        dontKnowLabel.setBounds(60, 210, 200, 25);
        dontKnowLabel.setForeground(Color.GREEN);
        loginPanel.add(dontKnowLabel);

        // 'login via access code' clickable label
        JLabel accessCodeLink = new JLabel("login via access code");
        accessCodeLink.setBounds(90, 230, 200, 25);
        accessCodeLink.setForeground(Color.GREEN);
        accessCodeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(accessCodeLink);

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if the username exists and the password matches
                if (users.containsKey(username) && users.get(username).password.equals(password)) {
                    mcUsername = users.get(username).mcUsername;
                    skinPath = users.get(username).skinPath;
                    errorLabel.setText("");
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Optionally handle or display mcUsername and skinPath
                } else {
                    errorLabel.setText("access denied");
                }
            }
        });

        // Add mouse listener to access code link
        accessCodeLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginPanel.setVisible(false);
                accessCodePanel.setVisible(true);
            }
        });
    }

    private static void initAccessCodePanel() {
        // Title label
        JLabel titleLabel = new JLabel("admin login");
        titleLabel.setBounds(100, 10, 100, 30);
        titleLabel.setForeground(Color.GREEN);
        accessCodePanel.add(titleLabel);

        // Access code label and text field
        JLabel accessCodeLabel = new JLabel("access code:");
        accessCodeLabel.setBounds(20, 50, 100, 25);
        accessCodeLabel.setForeground(Color.GREEN);
        accessCodePanel.add(accessCodeLabel);

        JTextField accessCodeField = new JTextField();
        accessCodeField.setBounds(120, 50, 150, 25);
        accessCodePanel.add(accessCodeField);

        // Login button
        JButton loginButton = new JButton("login");
        loginButton.setBounds(100, 90, 100, 25);
        accessCodePanel.add(loginButton);

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setBounds(20, 130, 250, 25);
        errorLabel.setForeground(Color.RED);
        accessCodePanel.add(errorLabel);

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accessCode = accessCodeField.getText();

                // Check if the access code is valid
                if (accessCodes.containsKey(accessCode)) {
                    mcUsername = accessCodes.get(accessCode).mcUsername;
                    skinPath = accessCodes.get(accessCode).skinPath;
                    errorLabel.setText("");
                    JOptionPane.showMessageDialog(frame, "Access granted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Optionally handle or display mcUsername and skinPath
                } else {
                    errorLabel.setText("access denied");
                }
            }
        });
    }

    // Getter methods for mcUsername and skinPath
    public static String getMcUsername() {
        return mcUsername;
    }

    public static String getSkinPath() {
        return skinPath;
    }

    // Internal class to store user details
    static class User {
        String username;
        String password;
        String mcUsername;
        String skinPath;

        User(String username, String password, String mcUsername, String skinPath) {
            this.username = username;
            this.password = password;
            this.mcUsername = mcUsername;
            this.skinPath = skinPath;
        }
    }
}
