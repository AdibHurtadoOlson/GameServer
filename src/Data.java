import java.io.Serializable;

/* This class takes in a DataObject converting it to a Data which can then be added to a DataPacket
 */

public class Data implements Serializable {
    private DataObject dataObject;

    public Data (DataObject dataObject) {
        this.dataObject = dataObject;
    }

    public DataObject getDataObject () {
        return dataObject;
    }
}
