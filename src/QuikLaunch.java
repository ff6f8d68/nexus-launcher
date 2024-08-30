
    import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    public class QuikLaunch {

        // Method to launch Minecraft with specified parameters
        public static void launchMinecraft(String username, String skinPath, int width, int height, int minMemory, int maxMemory, File minecraftDir) {
            List<String> launchCommand = new ArrayList<>();
            launchCommand.add("java");
            launchCommand.add("-Xms" + minMemory + "M");
            launchCommand.add("-Xmx" + maxMemory + "M");
            launchCommand.add("-cp");
            launchCommand.add(new File(minecraftDir, "minecraft.jar").getAbsolutePath() + ":" + new File(minecraftDir, "your_mod.jar").getAbsolutePath());
            launchCommand.add("net.minecraft.client.main.Main");
            launchCommand.add("--username");
            launchCommand.add(username);
            launchCommand.add("--width");
            launchCommand.add(String.valueOf(width));
            launchCommand.add("--height");
            launchCommand.add(String.valueOf(height));

            // Optional: Add any additional arguments here

            try {
                ProcessBuilder builder = new ProcessBuilder(launchCommand);
                builder.directory(minecraftDir);
                builder.inheritIO();
                Process process = builder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

