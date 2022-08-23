import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;

/*
This class is the server, it has an ArrayList of ServerSideSockets, processes information from them
and then outputs that data to the other users
 */

public class Server {
    private ArrayList<ServerSideSocket> serverSideSocketArrayList;
    private ArrayList<Thread> clientThreadArrayList;

    private ArrayList<DataPacket> allData;
    private ArrayList<ArrayList<DataPacket>> allData2D;

    boolean running;
    boolean serverConnectionReceiverIsAlive = false;
    private ServerSocket serverSocket;

    private ServerConnectionReceiver serverConnectionReceiver;
    private Thread serverConnectionReceiverThread;
    private static int port;

    public Server (int port) {
        this.port = port;
        running = true;

        this.clientThreadArrayList = new ArrayList<>();
        this.serverSideSocketArrayList = new ArrayList<>();
        this.allData2D = new ArrayList<>();
        this.allData = new ArrayList<>();

        this.serverSocket = null;
    }

    public void start () {
        System.out.println("Server Started");
        setUp();
        run();
    }

    private void setUp () {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run () {
        long start = System.currentTimeMillis();

        while (running) {
            if (!(serverConnectionReceiverIsAlive)) {
                if (serverConnectionReceiverThread != null) {
                    try {
                        serverConnectionReceiverThread.join();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Setting Up Connection Receiver");

                serverConnectionReceiver = new ServerConnectionReceiver(
                        serverSocket,
                        serverSideSocketArrayList,
                        clientThreadArrayList,
                        allData);

                serverConnectionReceiverThread = new Thread(serverConnectionReceiver);
                serverConnectionReceiverThread.start();
                serverConnectionReceiverIsAlive = true;
            }

            if (System.currentTimeMillis() - start >= 1000) {
                createAllData2D();
                processConnections();

                start = System.currentTimeMillis();
            }

            serverConnectionReceiverIsAlive = serverConnectionReceiver.getAlive();
        }
    }

    private void removeClient (ServerSideSocket serverSideSocket) {
        try {
            Thread toRemove = clientThreadArrayList.remove(serverSideSocketArrayList.indexOf(serverSideSocket));

            toRemove.join();

            serverSideSocketArrayList.remove(serverSideSocket);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void createAllData2D () {
        allData = new ArrayList<>();
        allData2D = new ArrayList<>();

        for (int counter = 0; counter < serverSideSocketArrayList.size(); counter++) {
            if (serverSideSocketArrayList.get(counter).isAlive()) {
                allData.add(serverSideSocketArrayList.get(counter).getData());

            } else {
                removeClient(serverSideSocketArrayList.get(counter));
                counter--;
            }
        }


        System.out.println(allData);

        for (int firstCounter = 0; firstCounter < allData.size(); firstCounter++) {
            ArrayList<DataPacket> tempDataList = new ArrayList<>();

            for (int secondCounter = 0; secondCounter < allData.size(); secondCounter++) {
                if (firstCounter != secondCounter) {
                    tempDataList.add(allData.get(secondCounter));
                }
            }

            if (tempDataList.size() == 0) {
                tempDataList.add(new DataPacket());
            }

            allData2D.add(tempDataList);
        }

        System.out.println(allData2D.toString());
        System.out.println("----------");
    }

    // Takes the allData2D ArrayList, and sends the correct ArrayList to the given ServerSideSocket
    private void processConnections () {
        if (serverSideSocketArrayList != null && serverSideSocketArrayList.size() != 0) {
            for (int counter = 0; counter < serverSideSocketArrayList.size(); counter++) {
                serverSideSocketArrayList.get(counter).setDataPacketArrayList(allData2D.get(counter));

            }
        }
    }
}
