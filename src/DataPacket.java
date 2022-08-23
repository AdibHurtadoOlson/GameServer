import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/* This class takes in Data and adds it to the packet ArrayList, gets all the info from the Data
and returns it
 */
public class DataPacket implements Serializable {
    private ArrayList<DataObject> packet;

    public DataPacket () {
        this.packet = new ArrayList<>();
    }

    public void addToPacket (DataObject data) {
        packet.add(data);
    }

    public ArrayList<DataObject> getPacket () {
        return packet;
    }

    @Override
    public String toString () {
        String result = "[";

        for (DataObject data : packet) {
            result = result + ((StringObj)data).toString() + ", ";
        }

        result = result + "]";

        return result;
    }
}
