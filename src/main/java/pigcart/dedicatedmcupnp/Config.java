package pigcart.dedicatedmcupnp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Config implements Serializable {
    public static ArrayList<Integer> tcpPorts = new ArrayList<>();
    public static ArrayList<Integer> udpPorts = new ArrayList<>();
    public static int refreshIntervalMinutes = 30; // Default: 30 minutes

    private static final Path CONFIG_FILE = Paths.get("config", "upnp.txt");

    public static void readConfig() {
        if (!Files.exists(CONFIG_FILE)) {
            createDefaultConfig();
        }
        try (BufferedReader br = Files.newBufferedReader(CONFIG_FILE)) {
            String line;
            while((line = br.readLine()) != null) {
                if (!line.isBlank() && !line.startsWith("#")) {
                    if (line.startsWith("TCP ")) {
                        tcpPorts.add(Integer.valueOf(line.substring(4)));
                    } else if (line.startsWith("UDP ")) {
                        udpPorts.add(Integer.valueOf(line.substring(4)));
                    } else if (line.startsWith("REFRESH_INTERVAL ")) {
                        try {
                            refreshIntervalMinutes = Integer.valueOf(line.substring(17).trim());
                            if (refreshIntervalMinutes < 0) {
                                DedicatedMCUpnp.LOGGER.warn("Invalid refresh interval: {}. Using default of 30 minutes.", refreshIntervalMinutes);
                                refreshIntervalMinutes = 30;
                            }
                        } catch (NumberFormatException e) {
                            DedicatedMCUpnp.LOGGER.error("Malformed refresh interval. Using default of 30 minutes.", e);
                            refreshIntervalMinutes = 30;
                        }
                    } else {
                        createDefaultConfig();
                        DedicatedMCUpnp.LOGGER.error("Invalid config entry: {}", line);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            DedicatedMCUpnp.LOGGER.error("Error loading config: {}", String.valueOf(e));
        } catch (NumberFormatException e) {
            createDefaultConfig();
            DedicatedMCUpnp.LOGGER.error("Malformed config file. {}", String.valueOf(e));
        }

        // delete old version of config file
        try {
            Path oldFile = Paths.get("config", "upnp.yaml");
            if (Files.exists(oldFile)) {
                DedicatedMCUpnp.LOGGER.info("Deleting old config");
                Files.delete(oldFile);
            }
        } catch (IOException e) {
            DedicatedMCUpnp.LOGGER.error("Error deleting old config: {}", String.valueOf(e));
        }
    }
    private static void createDefaultConfig() {
        try {
            if (!Files.exists(CONFIG_FILE.getParent())) {
                Files.createDirectories(CONFIG_FILE.getParent().toAbsolutePath());
            }
            OutputStream out = Files.newOutputStream(CONFIG_FILE);
            InputStream defaultConfigInputStream = DedicatedMCUpnp.class.getClassLoader().getResourceAsStream("default_ports_config.txt");
            if (defaultConfigInputStream == null) {
                throw new IOException("Could not load default ports config");
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = defaultConfigInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            defaultConfigInputStream.close();
            out.close();
        } catch (IOException e) {
            DedicatedMCUpnp.LOGGER.error("Error creating default config: {}", String.valueOf(e));
        }
    }
}