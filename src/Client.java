import java.util.ArrayList;

/*
This class is a Client. Normally, this is where the client's game instance would be. The only necessary functions
are the setClientConnectionReceiver (to tie the ClientConnectionReceiver to this client) and the run method
created through the Runnable interface
 */

public class Client implements Runnable {
    private ClientConnectionReceiver clientConnectionReceiver;

    private ArrayList<DataPacket> dataPacketArrayList;

    private boolean running = true;

    private ArrayList<StringObj> temp = new ArrayList<>();


    public Client () {
        temp.add(new StringObj("that"));
        temp.add(new StringObj("there"));
        temp.add(new StringObj("think"));
        temp.add(new StringObj("than"));
        temp.add(new StringObj("though"));
    }

    public void setClientConnectionReceiver (ClientConnectionReceiver clientConnectionReceiver) {
        this.clientConnectionReceiver = clientConnectionReceiver;
    }

    @Override
    public void run () {
        long start = System.currentTimeMillis();
        temp.forEach(str -> clientConnectionReceiver.loadPacket(str));


        while (running) {
            if (System.currentTimeMillis() - start >= 1000) {
                if (clientConnectionReceiver == null) {
                    continue;

                } else if (dataPacketArrayList == null) {
                    dataPacketArrayList = clientConnectionReceiver.getReceiveDataPacketArrayList();

                } else {
                    try {
                        dataPacketArrayList = clientConnectionReceiver.getReceiveDataPacketArrayList();
                        System.out.println(dataPacketArrayList.get(dataPacketArrayList.size()).getPacket().toString());

                    } catch (Exception e) {
                        System.out.println(dataPacketArrayList.toString());
                    }
                }

                start = System.currentTimeMillis();
            }
        }
    }
}
