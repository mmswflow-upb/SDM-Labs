package Part_1;

import java.io.*;
import java.net.*;

public class MyServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is running and waiting for a connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection accepted: " + socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                out.println(line); // echo back to client
                if (line.equals("END")) break;
            }

            socket.close();
        }
    }
}
