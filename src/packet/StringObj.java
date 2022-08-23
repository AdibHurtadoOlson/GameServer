package packet;/*
This class serves as a test to see if Objects that implement packet.DataObject can be passes in a
serialized fashion
 */

import packet.DataObject;

public class StringObj implements DataObject {
    private String str;

    public StringObj (String str) {
        this.str = str;
    }

    @Override
    public String toString () {
        return str;
    }
}
