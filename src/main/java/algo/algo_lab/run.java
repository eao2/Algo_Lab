package algo.algo_lab;

public class run {
    public static void main(String[] args) {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                Server.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        // Start multiple client threads
        for (int i = 0; i < 1; i++) {
            final int clientId = i;
            Thread clientThread = new Thread(() -> {
                try {
//                    Client.main(new String[]{String.valueOf(clientId)});
                    Admin.main(new String[]{String.valueOf(clientId)});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();
        }
    }
}
