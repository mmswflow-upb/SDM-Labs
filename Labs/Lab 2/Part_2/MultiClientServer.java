package Part_2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiClientServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        // Thread-safe list of client output streams
        List<PrintWriter> clientOutputs = new CopyOnWriteArrayList<>();

        // Thread to accept clients
        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientOutputs.add(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Main loop: read input from server console and broadcast it
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = consoleInput.readLine()) != null) {
            for (PrintWriter out : clientOutputs) {
                out.println(message);
            }
            System.out.println("Sent to all clients: " + message);
        }

        // Server shutdown logic could go here if needed
    }
}
