package Part_2;

import java.io.*;
import java.net.*;

public class MultiClientServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("MultiClientServer running on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection accepted: " + socket);
            new Thread(new ClientHandler(socket)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received from " + socket + ": " + line);
                out.println(line); // echo
                if (line.equals("END")) break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

