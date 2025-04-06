package Part_1;

import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        Socket socket = new Socket("localhost", PORT);
        System.out.println("socket = " + socket);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        for (int i = 1; i <= 10; i++) {
            String message = "I am " + i;
            out.println(message);
            String response = in.readLine();
            System.out.println("Received from server: " + response);
        }

        out.println("END");
        String endResponse = in.readLine();
        System.out.println("Received from server: " + endResponse);

        socket.close();
    }
}
