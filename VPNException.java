import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Define interfaces
interface VPNClient {
    void connect();
    //    void sendData(byte[] data);
    void disconnect();
}


// Define abstract class
abstract class VPNConnection {
    protected String serverAddress;
    protected int port;

    public VPNConnection(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    abstract void establishConnection();
    abstract void closeConnection();

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }
}

// Implement inheritance
class BasicVPNConnection extends VPNConnection {
    public BasicVPNConnection(String serverAddress, int port) {
        super(serverAddress, port);
    }

    @Override
    void establishConnection() {
        // Logic to establish connection
        System.out.println("Connection established to " + serverAddress + ":"+ port);
    }

    @Override
    void closeConnection() {
        // Logic to close connection
        System.out.println("Connection closed");
    }
}

// Implement encapsulation
class VPNClientImpl implements VPNClient {
    private VPNConnection connection;

    public VPNClientImpl(VPNConnection connection) {
        this.connection = connection;
    }

    @Override
    public void connect() {
        connection.establishConnection();
    }



    @Override
    public void disconnect() {
        connection.closeConnection();
    }
}

// Implement multithreading
class VPNServer {
    private int port;
    private boolean isRunning;
    private Map<String, VPNClient> clients;
    private Object lock = new Object(); // Declare lock
    private boolean websiteOpened = false; // Declare websiteOpened flag
    private String serverAddress; // Store server address

    public VPNServer(String serverAddress,int port)
    {
        this.serverAddress = serverAddress;
        this.port = port;
        this.clients = new HashMap<>();
    }

    public void start() throws VPNException {
        isRunning = true;
        System.out.println("VPN server started on port " + port);
        new Thread(this::handleClientConnections).start();
    }


    public void stop() {
        isRunning = false;
        System.out.println("VPN server stopped");
    }

    private boolean shouldOpenURL(String serverAddress)
    {
        // Extract the first octet from the server address
        String[] octets = serverAddress.split("\\.");
        int firstOctet = Integer.parseInt(octets[0]);

        // Check if the first octet falls within class B range
        return (firstOctet >= 128 && firstOctet <= 191);
    }

    // Add this method to the VPNServer class
    private void openWebsite(String serverAddress) throws VPNException {
        try {
            URI url = new URI("https://rvu.edu.in/");

            String[] octets = serverAddress.split("\\.");
            int firstOctet = Integer.parseInt(octets[0]);

            // Check if the first octet falls within class B range
            if (firstOctet >= 128 && firstOctet <= 191) {
                // Proceed to open the website if the IP address belongs to Class B range
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(url);
                        System.out.println("Website opened successfully.");
                    } else {
                        System.out.println("Desktop browsing is not supported on this platform.");
                    }
                } else {
                    System.out.println("Desktop is not supported on this platform.");
                }
            } else {
                // If the IP address does not belong to Class B, throw VPNException
                throw new VPNException("Server address is not in Class B range.");
            }
        } catch (Exception e) {
            // Catch and rethrow other exceptions as VPNException
            throw new VPNException("Error opening website: " + e.getMessage());
        }
    }

    private void handleClientConnections() {
        while (isRunning) {
            // Simulate accepting client connections
            VPNConnection connection;

            // Simulate successful connection for certain IP addresses
            if (Math.random() < 0.7) { // 70% chance of successful connection
                connection = new BasicVPNConnection(serverAddress, port);
                System.out.println("Client connected successfully.");
            }
            else { // Simulate failed connection for other IP addresses
                connection = new BasicVPNConnection(serverAddress, port);
                System.out.println("Failed to connect. VPN server rejected the connection.");
            }

            VPNClient client = new VPNClientImpl(connection);
            client.connect();

            try {
                String[] octets = connection.getServerAddress().split("\\.");
                int firstOctet = Integer.parseInt(octets[0]);

                // Ensure that only one thread attempts to open the website
                synchronized (lock) {
                    if (!websiteOpened) {
                        System.out.println("Server address is in Class B range.");
                        openWebsite(connection.getServerAddress()); // Pass the server address to openWebsite method
                        websiteOpened = true; // Set the flag to true to indicate that the website has been opened
                        stop();
                    }
                }
            }
            catch (Throwable e) {
                // Handle any exceptions that occur during website opening or VPN connection
                System.out.println("Error: " + e.getMessage());
            }
            finally {
                // Disconnect the client regardless of whether an exception occurred or not
                client.disconnect();
            }
        }
    }
}

// Custom Exception class
class VPNException extends Exception {
    public VPNException(String message) {
        super(message);
    }
}

// VPNTester class to test VPN connectivity
public class VPNException {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the server IP address:");
        String serverAddress = sc.nextLine();

        System.out.print("Enter the server port:");
        int port = sc.nextInt();

        System.out.println("SERVER IP: " + serverAddress);
        System.out.println("SERVER PORT: " + port);

        try {
            VPNServer server = new VPNServer(serverAddress, port);
            server.start();
        } catch (VPNException e) {
            System.err.println("VPN Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}