package Part_2;

import java.io.*;
import java.net.*;

public class MonoClient extends Thread {
    private int clientId;

    public MonoClient(int id) {
        this.clientId = id;
    }

    public void run() {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Client " + clientId + " connected: " + socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Client " + clientId + " received: " + line);
                if ("END".equalsIgnoreCase(line)) {
                    break;
                }
            }

            socket.close();
            System.out.println("Client " + clientId + " disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
