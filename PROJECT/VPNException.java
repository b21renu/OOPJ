import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;

interface VPNClient {
    void connect();
    void disconnect();
}

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

class BasicVPNConnection extends VPNConnection {
    public BasicVPNConnection(String serverAddress, int port) {
        super(serverAddress, port);
    }

    @Override
    void establishConnection() {
        System.out.println("Connection established to " + serverAddress + ":"+ port);
    }

    @Override
    void closeConnection() {
        System.out.println("Connection closed");
    }
}

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

class VPNServer {
    private int port;
    private boolean isRunning;
    private Object lock = new Object();
    private boolean websiteOpened = false;
    private String serverAddress;

    public VPNServer(String serverAddress,int port) {
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

    private boolean shouldOpenURL(String serverAddress) {
        String[] octets = serverAddress.split("\\.");
        int firstOctet = Integer.parseInt(octets[0]);
        return (firstOctet >= 128 && firstOctet <= 191);
    }

    private void openWebsite(String serverAddress) throws VPNException {
        try {
            URI url = new URI("https://rvu.edu.in/");
            String[] octets = serverAddress.split("\\.");
            int firstOctet = Integer.parseInt(octets[0]);
            if (firstOctet >= 128 && firstOctet <= 191) {
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
                throw new VPNException("Server address is not in Class B range.");
            }
        } catch (Exception e) {
            throw new VPNException("Error opening website: " + e.getMessage());
        }
    }

    private void handleClientConnections() {
        while (isRunning) {
            VPNConnection connection;
            if (Math.random() < 0.7) {
                connection = new BasicVPNConnection(serverAddress, port);
                System.out.println("Client connected successfully.");
            } else {
                connection = new BasicVPNConnection(serverAddress, port);
                System.out.println("Failed to connect. VPN server rejected the connection.");
            }

            VPNClient client = new VPNClientImpl(connection);
            client.connect();

            try {
                String[] octets = connection.getServerAddress().split("\\.");
                int firstOctet = Integer.parseInt(octets[0]);
                synchronized (lock) {
                    if (!websiteOpened) {
                        System.out.println("Server address is in Class B range.");
                        openWebsite(connection.getServerAddress());
                        websiteOpened = true;
                        stop();
                    }
                }
            } catch (Throwable e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                client.disconnect();
            }
        }
    }
}

class VPNException extends Exception {
    public VPNException(String message) {
        super(message);
    }
}

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
