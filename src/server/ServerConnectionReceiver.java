package server;

import packet.DataPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/* The purpose of the class is to sit on a Thread, waiting for a client_network.Client connection to the server to be established
 once a connection is established, it adds the client_network.Client Thread to the server.Server ArrrayList<Thread>
*/

public class ServerConnectionReceiver implements Runnable {
    private Boolean isAlive;

    private Socket socket;

    private int count = 0;

    private ServerSocket serverSocket;

    private ArrayList<DataPacket> allData;
    private ArrayList<ServerSideSocket> serverSideSocketArrayList;
    private ArrayList<Thread> clientThreadArrayList;

    public ServerConnectionReceiver (
            ServerSocket serverSocket,
            ArrayList<ServerSideSocket> serverSideSocketArrayList,
            ArrayList<Thread> clientThreadArrayList,
            ArrayList<DataPacket> allData) {

        this.isAlive = true;
        this.serverSocket = serverSocket;
        this.serverSideSocketArrayList = serverSideSocketArrayList;
        this.clientThreadArrayList = clientThreadArrayList;
        this.allData = allData;
    }

    public boolean getAlive () {
        return isAlive;
    }

    @Override
    public void run() {
        System.out.println("Opening New Connection");

        try {
            this.socket = serverSocket.accept();

            ServerSideSocket serverSideSocket = new ServerSideSocket(socket, count++, allData);
            serverSideSocketArrayList.add(serverSideSocket);

            Thread clientThread = new Thread(serverSideSocket);
            clientThread.start();
            clientThreadArrayList.add(clientThread);

            System.out.println("Connection Established");

        }  catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Closing ConnectionReceiver");
        System.out.println("----------");
        isAlive = false;
    }
}
