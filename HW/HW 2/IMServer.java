import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class IMServer {
    private static final int PORT = 12345;
    // Thread-safe list to maintain all connected clients
    private static List<ServerWorker> workers = new CopyOnWriteArrayList<>();
    // Set to track unique client IDs
    private static Set<String> clientIds = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("IM Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                ServerWorker worker = new ServerWorker(socket);
                workers.add(worker);
                new Thread(worker).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    // Broadcast a message to all connected clients
    public static void broadcast(String message) {
        for (ServerWorker worker : workers) {
            worker.sendMessage(message);
        }
    }

    // Remove a worker when a client disconnects
    public static void removeWorker(ServerWorker worker) {
        workers.remove(worker);
        if (worker.clientId != null) {
            clientIds.remove(worker.clientId);
        }
    }

    // Check if a client ID is available
    public static synchronized boolean isClientIdAvailable(String clientId) {
        return !clientIds.contains(clientId);
    }

    // Add a client ID to the set
    public static synchronized boolean addClientId(String clientId) {
        return clientIds.add(clientId);
    }

    // Inner class to handle each client connection
    private static class ServerWorker implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String clientId;

        public ServerWorker(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Error initializing worker: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                // First message should be the client ID
                String initialMessage = in.readUTF();
                if (initialMessage.startsWith("ID:")) {
                    String proposedId = initialMessage.substring(3);
                    if (isClientIdAvailable(proposedId)) {
                        this.clientId = proposedId;
                        addClientId(proposedId);
                        out.writeUTF("ID_ACCEPTED");
                        out.flush();
                        System.out.println("Client " + proposedId + " connected successfully");
                    } else {
                        out.writeUTF("ID_REJECTED");
                        out.flush();
                        System.out.println("Client ID " + proposedId + " rejected - already in use");
                        socket.close();
                        return;
                    }
                } else {
                    System.out.println("Invalid initial message format");
                    socket.close();
                    return;
                }

                while (true) {
                    // Read a message sent by a client
                    String message = in.readUTF();
                    System.out.println("Received from " + clientId + ": " + message);
                    // Broadcast the message to all connected clients
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                removeWorker(this);
            }
        }

        public void sendMessage(String message) {
            try {
                out.writeUTF(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Error sending message to client: " + e.getMessage());
            }
        }
    }
}
