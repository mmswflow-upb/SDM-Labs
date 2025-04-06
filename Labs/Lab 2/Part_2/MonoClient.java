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
            System.out.println("Client " + clientId + " socket = " + socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            for (int i = 1; i <= 10; i++) {
                String message = "Client " + clientId + ": I am " + i;
                out.println(message);
                String response = in.readLine();
                System.out.println("Client " + clientId + " received: " + response);
            }

            out.println("END");
            System.out.println("Client " + clientId + " received: " + in.readLine());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
