package global;

import java.io.Serializable;

public class Message implements Serializable {

    // message types
    public static final int CLIENT_CONNECTED = 1,
            CLIENT_DISCONNECTED = 2,
            DUPLICATE_THREAD = 3;

    private int type;
    private String message;

    public Message(int type) {
        this.type = type;
    }

    public Message(String message, int type) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
