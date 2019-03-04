package beans;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mail implements Serializable {

    private final String sender;
    private final String message;
    private final String receiver;
    private final String subject;
    private final LocalDateTime date;

    public Mail(String sender, String receiver, String message, String subject) {
        this.date = LocalDateTime.now();
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.subject = subject;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public String getReceiver() {
        return receiver;
    }

}
