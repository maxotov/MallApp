package kz.itdamu.mallapp.entity;

/**
 * Created by Aibol on 13.03.2016.
 */
public class Message {
    private boolean error;
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
