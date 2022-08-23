package client_network;

import client_network.Client;
import client_network.ClientConnectionReceiver;

import java.util.ArrayList;

/*
This class serves as the start for the client_network.Client, it creates a client_network.Client and, as of now, creates a separate
client_network.ClientConnectionReceiver, and ties it to the client_network.Client
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
