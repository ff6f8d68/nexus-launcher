package home;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

public class TelescopeApp extends JFrame {

    private JButton telescopeButton;

    public TelescopeApp() {
        // Set up the main frame
        setTitle("Telescope to the Universe");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create the Telescope button
        telescopeButton = new JButton("Telescope");
        telescopeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUniverseWindow();
            }
        });

        // Add the button to the frame
        add(telescopeButton);

        setVisible(true);
    }

    private void openUniverseWindow() {
        // Create a new window for the universe map
        JFrame universeFrame = new JFrame("Universe Map");
        universeFrame.setSize(800, 600);
        universeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to display planets
        JPanel universePanel = new JPanel();
        universePanel.setLayout(new GridLayout(3, 3)); // Simple grid layout for demonstration

        // Get the list of Minecraft servers (This would normally be fetched from an API)
        List<MinecraftServer> servers = getMinecraftServers();

        // Add each server as a planet
        for (MinecraftServer server : servers) {
            JPanel planetPanel = new JPanel(new BorderLayout());
            JLabel planetIcon = new JLabel(new ImageIcon(server.getIcon()));
            JLabel planetName = new JLabel(server.getName(), SwingConstants.CENTER);

            planetPanel.add(planetIcon, BorderLayout.CENTER);
            planetPanel.add(planetName, BorderLayout.SOUTH);

            universePanel.add(planetPanel);
        }

        universeFrame.add(new JScrollPane(universePanel));
        universeFrame.setVisible(true);
    }

    private List<MinecraftServer> getMinecraftServers() {
        // Dummy implementation: Replace with actual API calls
        // Example: Querying a server to get its name and icon
        return List.of(
                queryMinecraftServer("example.com", 25565),
                queryMinecraftServer("anotherexample.com", 25565)
        );
    }

    private MinecraftServer queryMinecraftServer(String ip, int port) {
        try {
            // Replace with actual Minecraft server query logic
            MineStat ms = new MineStat(ip, port);
            String serverName = ms.getServerName();
            BufferedImage serverIcon = getServerIcon(ms.getBase64Icon());
            return new MinecraftServer(serverName, serverIcon);
        } catch (Exception e) {
            e.printStackTrace();
            return new MinecraftServer("Unknown", null);
        }
    }

    private BufferedImage getServerIcon(String base64Icon) {
        try {
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Icon);
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new TelescopeApp();
    }
}

class MinecraftServer {
    private String name;
    private BufferedImage icon;

    public MinecraftServer(String name, BufferedImage icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getIcon() {
        return icon;
    }
}

// Hypothetical class to demonstrate querying a Minecraft server
class MineStat {
    private String serverName;
    private String base64Icon;

    public MineStat(String ip, int port) {
        // Implement the logic to query the Minecraft server
        // Set serverName and base64Icon from the server response
        serverName = "Minecraft Server";
        base64Icon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAIAAAD...";
    }

    public String getServerName() {
        return serverName;
    }

    public String getBase64Icon() {
        return base64Icon;
    }
}

