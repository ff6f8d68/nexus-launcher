import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class main extends JFrame {
    private JComboBox<String> versionComboBox;
    private JTextField usernameField;
    private JTextArea terminalArea;
    private JTextField commandField;
    private Set<String> bannedUsers;
    private Map<String, Queue<String>> messageQueues;
    private boolean loggedIn = false; // Track if the user is logged in
    private JButton joinButton; // Reference to the "Join Game" button

    public main() {
        setTitle("Nexus Minecraft Launcher");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        initializeComponents();

        // Load data after components are initialized
        loadData();

        // Add shutdown hook to save data when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData));
    }

    private void initializeComponents() {
        bannedUsers = new HashSet<>();
        messageQueues = new HashMap<>();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(5);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);

        JLabel infoLabel = new JLabel(
                "<html><div style='text-align: left; color: green;'>" +
                        "This is an experimental version of Minecraft,<br>" +
                        "enter your username and not anyone else's<br>" +
                        "Don't get <b>BANNED</b><br>" +
                        "Enjoy the game without violating our<br>" +
                        "<b>RULES</b></div></html>",
                SwingConstants.LEFT
        );
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(infoLabel, BorderLayout.NORTH);

        usernameField = new JTextField("Enter Username");
        infoPanel.add(usernameField, BorderLayout.SOUTH);

        leftPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        terminalArea = new JTextArea();
        terminalArea.setEditable(false);
        terminalArea.setBackground(Color.BLACK);
        terminalArea.setForeground(Color.GREEN);
        terminalArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane terminalScrollPane = new JScrollPane(terminalArea);
        rightPanel.add(terminalScrollPane, BorderLayout.CENTER);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBackground(Color.BLACK);
        commandField = new JTextField();
        commandPanel.add(commandField, BorderLayout.CENTER);

        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText().trim();
                processCommand(command);
                commandField.setText("");
            }
        });

        rightPanel.add(commandPanel, BorderLayout.SOUTH);
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        versionComboBox = new JComboBox<>(new String[]{"Nexus1", "Nexus2", "Nexus3"}); // Example versions
        bottomPanel.add(versionComboBox);

        // Create the "Log In" button
        JButton loginButton = new JButton("Log In");
        bottomPanel.add(loginButton);

        // Create the "Join Game" button
        joinButton = new JButton("Join Game");
        joinButton.setEnabled(false); // Disabled until the user logs in
        bottomPanel.add(joinButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listener for the "Log In" button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                if (username.length() > 20) {
                    JOptionPane.showMessageDialog(main.this, "Username must be 20 characters or less.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!username.isEmpty()) {
                    if (bannedUsers.contains(username)) {
                        updateTerminal("You have been banned by a developer");
                    } else {
                        loggedIn = true;
                        updateTerminal("You are logged in as " + username);
                        displayStoredMessages(username); // Display stored messages for the user
                        joinButton.setEnabled(true); // Enable the "Join Game" button
                    }
                } else {
                    JOptionPane.showMessageDialog(main.this, "Please enter a username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for the "Join Game" button
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedIn) {
                    String username = usernameField.getText().trim();
                    String version = (String) versionComboBox.getSelectedItem();
                    launchMinecraft(username, version);
                }
            }
        });
    }

    private void launchMinecraft(String username, String version) {
        String jarPath = "";
        switch (version) {
            case "Nexus1":
                jarPath = "path/to/Nexus1.jar";
                break;
            case "Nexus2":
                jarPath = "path/to/Nexus2.jar";
                break;
            case "Nexus3":
                jarPath = "path/to/Nexus3.jar";
                break;
            default:
                updateTerminal("Unknown version selected: " + version);
                return;
        }

        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            updateTerminal("JAR file not found: " + jarPath);
            return;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath);
            pb.directory(new File("path/to/minecraft/directory")); // Set working directory if needed
            pb.redirectErrorStream(true);
            Process process = pb.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        updateTerminal(line);
                    }
                } catch (IOException e) {
                    updateTerminal("Error reading process output: " + e.getMessage());
                }
            }).start();

            updateTerminal("Launching Minecraft with version " + version);
        } catch (IOException e) {
            updateTerminal("Failed to launch Minecraft: " + e.getMessage());
        }
    }

    private void updateTerminal(String message) {
        if (terminalArea != null) {
            terminalArea.append(message + "\n");
            terminalArea.setCaretPosition(terminalArea.getDocument().getLength());
        } else {
            System.err.println("Terminal area is not initialized.");
        }
    }

    private void processCommand(String command) {
        if (command.startsWith("/ban ")) {
            String usernameToBan = command.substring(5).trim();
            bannedUsers.add(usernameToBan);
            updateTerminal("Banned " + usernameToBan);
        } else if (command.startsWith("/unban ")) {
            String usernameToUnban = command.substring(7).trim();
            bannedUsers.remove(usernameToUnban);
            updateTerminal("Unbanned " + usernameToUnban);
        } else if (command.startsWith("/chat ")) {
            String[] parts = command.substring(6).trim().split(" ", 2);
            if (parts.length == 2) {
                String recipient = parts[0].trim();
                String message = parts[1].trim();
                Queue<String> recipientQueue = messageQueues.computeIfAbsent(recipient, k -> new LinkedList<>());
                recipientQueue.add(usernameField.getText() + " texted you: " + message);
                updateTerminal("You texted " + recipient + ": " + message);
            } else {
                updateTerminal("Invalid chat command. Use: /chat <user> <message>");
            }
        } else if (command.equals("/banlist")) {
            updateTerminal("Banned users: " + String.join(", ", bannedUsers));
        } else if (command.equals("/help")) {
            updateTerminal("Available commands: /ban <user>, /unban <user>, /chat <user> <message>, /banlist, /help");
        } else {
            updateTerminal("Unknown command: " + command);
        }
    }

    private void displayStoredMessages(String username) {
        Queue<String> messages = messageQueues.get(username);
        if (messages != null) {
            while (!messages.isEmpty()) {
                updateTerminal(messages.poll());
            }
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
            oos.writeObject(bannedUsers);
            oos.writeObject(messageQueues);
        } catch (IOException e) {
            updateTerminal("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))) {
            bannedUsers = (Set<String>) ois.readObject();
            messageQueues = (Map<String, Queue<String>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            updateTerminal("Error loading data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            main launcher = new main();
            launcher.setVisible(true);
        });
    }
}
