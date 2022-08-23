import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/* The purpose of this class is to act as the in-between for the server and client connection. This class takes in
all the data the client outputs, processes it, and sends other data to the client
 */

public class ServerSideSocket implements Runnable {
    private static int id = 0;

    private static boolean hasReceivedDataPacket = false;
    private boolean hasSentDataPacket;
    private int thisId;
    private boolean isAlive;
    private Socket socket;

    private DataPacket receiveDataPacket;
    private ArrayList<DataPacket> sendDataPacketArrayList;
    private ArrayList<DataPacket> allServerData;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private int location;

    public ServerSideSocket (Socket socket, int location, ArrayList<DataPacket> allServerData) {
        this.location = location;
        this.isAlive = true;
        this.hasSentDataPacket = false;
        this.socket = socket;
        this.sendDataPacketArrayList = new ArrayList<>();
        this.allServerData = allServerData;
        thisId = id++;
    }

    public void setAlive (boolean isAlive) {
        this.isAlive = isAlive;
    }

    private void setUp () {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDataPacket () {
        try {

            receiveDataPacket = (DataPacket) objectInputStream.readObject();
            allServerData.add(receiveDataPacket);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataPacket getData () {
        return receiveDataPacket;
    }

    public void setDataPacketArrayList (ArrayList<DataPacket> dataPacketArrayList) {
        sendDataPacketArrayList = dataPacketArrayList;
    }

    private void processData () {
        try {
            objectOutputStream.writeObject(sendDataPacketArrayList);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        setUp();

        while (isAlive) {
            getDataPacket();
            processData();
        }
    }
}
