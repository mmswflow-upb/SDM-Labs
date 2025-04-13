import java.io.*;
import java.net.*;
import java.util.Scanner;

public class IMClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private String clientId;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public IMClient(String clientId) {
        this.clientId = clientId;
    }

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Send client ID first
            out.writeUTF("ID:" + clientId);
            out.flush();

            // Wait for server response
            String response = in.readUTF();
            if (response.equals("ID_REJECTED")) {
                System.out.println(
                        "Error: Client ID '" + clientId + "' is already in use. Please choose a different ID.");
                socket.close();
                return;
            }

            System.out.println("Connected to the server as " + clientId);

            // Start a new thread to continuously listen for incoming messages
            new Thread(new IncomingReader()).start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Read user input in the format "receiverID:message text"
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit"))
                    break;
                // Create the message in the format "senderID:receiverID:message text"
                String messageToSend = clientId + ":" + userInput;
                sendMessage(messageToSend);
            }
            scanner.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error in client: " + e.getMessage());
        }
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    // Runnable inner class to handle incoming messages from the server
    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readUTF();
                    // Message format: senderID:receiverID:message text
                    String[] parts = message.split(":", 3);
                    if (parts.length < 3) {
                        System.out.println("Malformed message received: " + message);
                        continue;
                    }
                    String sender = parts[0];
                    String receiver = parts[1];
                    String messageText = parts[2];
                    // Only display the message if it's destined for this client
                    if (receiver.equals(clientId)) {
                        System.out.println("Message from " + sender + ": " + messageText);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java IMClient <clientId>");
            return;
        }
        IMClient client = new IMClient(args[0]);
        client.start();
    }
}
