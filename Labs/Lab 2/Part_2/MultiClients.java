package Part_2;

public class MultiClients {
    public static void main(String[] args) {
        int clientCount = 3; // or any number of clients you want to simulate
        for (int i = 1; i <= clientCount; i++) {
            new MonoClient(i).start();
        }
    }
}
