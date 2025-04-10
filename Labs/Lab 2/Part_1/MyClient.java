package Part_1;

import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        Socket socket = new Socket("localhost", PORT);
        System.out.println("Connected to server: " + socket);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Type messages to send to the server. Type 'END' to finish.");

        String userInput;
        while ((userInput = consoleReader.readLine()) != null) {
            out.println(userInput);
            String response = in.readLine();
            System.out.println("Received from server: " + response);
            if ("END".equalsIgnoreCase(userInput)) {
                break;
            }
        }

        socket.close();
        System.out.println("Connection closed.");
    }
}
