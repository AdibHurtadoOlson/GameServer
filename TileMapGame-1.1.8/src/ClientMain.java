import java.util.ArrayList;

/*
This class serves as the start for the Client, it creates a Client and, as of now, creates a separate
ClientConnectionReceiver, and ties it to the Client
 */

public class ClientMain {
    public static void main (String[] args) {
        ArrayList<Thread> threadArrayList = new ArrayList<>();

        Client client = new Client();
        ClientConnectionReceiver clientConnectionReceiver = new ClientConnectionReceiver("127.0.0.1", 5000);

        client.setClientConnectionReceiver(clientConnectionReceiver);

        Thread clientThread = new Thread(client);
        threadArrayList.add(clientThread);
        Thread clientConnectionReceiverThread = new Thread(clientConnectionReceiver);
        threadArrayList.add(clientConnectionReceiverThread);

        for (Thread thread : threadArrayList) {
            thread.start();
        }
    }
}
