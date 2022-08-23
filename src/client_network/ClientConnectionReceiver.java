package client_network;

import packet.DataObject;
import packet.DataPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientConnectionReceiver implements Runnable {
    private Socket socket;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private DataPacket sendDataPacket;

    private ArrayList<DataPacket> receiveDataPacketArrayList;

    private boolean running;

    private long startTime;

    private String address;

    private int port;

    public ClientConnectionReceiver (String address, int port) {
        this.address = address;
        this.port = port;
        running = true;

        sendDataPacket = new DataPacket();

        try {
            socket = new Socket(this.address, this.port);
            System.out.println("Connected");

        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init () {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(socket.getInputStream());

        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPacket () {
        try {
            objectOutputStream.writeObject(sendDataPacket);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendDataPacket = new DataPacket();
    }

    private void receiveDataPacketArrayList () {
        try {
            receiveDataPacketArrayList = (ArrayList<DataPacket>) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadPacket (DataObject dataObject) {
        sendDataPacket.addToPacket(dataObject);
    }

    public ArrayList<DataPacket> getReceiveDataPacketArrayList() {
        return receiveDataPacketArrayList;
    }

    @Override
    public void run () {
        init();
        startTime = System.currentTimeMillis();

        while (running) {
            if (System.currentTimeMillis() - startTime >= 1000) {
                sendPacket();
                receiveDataPacketArrayList();

                startTime = System.currentTimeMillis();
            }
        }

        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
