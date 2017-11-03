package pomelo.kenttan.shadowphone.model;

/**
 * Created by kenttan on 03-Nov-17.
 */

public class SmsModel {
    private String sender;
    private String text;

    public SmsModel(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
