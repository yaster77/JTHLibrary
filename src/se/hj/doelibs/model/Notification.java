package se.hj.doelibs.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Christoph
 */
public class Notification implements Serializable {

    private int notificationId;
    private User recipient;
    private User sender; //sender can be null in case of the notification was send by the system
    private boolean read;
    private String message;
    private Date sendDate;
    private NotificationType type;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Enum for type of the notification
     *
     * @author Christoph
     */
    public enum NotificationType {
        /**
         * type which will be used for messages which are related to accepting a users registration
         */
        REGISTRATION_ACCEPT_REQUEST(0),

        /**
         * this type will be used for messages which are related with the reservation of a title
         */
        TITLE_RESERVATION(1),

        /**
         * this type will be used for messages which are related to inform that a recall has expired
         */
        EXPIRED_RECALL(2),

        /**
         * this type of message tells a user their book has been recalled
         */
        RECALL(3),

        /**
         * this is a fallback
         */
        UNKNOWN(-1);

        private int value;
        private NotificationType(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }

        public static NotificationType getType(int val) {
            for (NotificationType type : NotificationType.values()) {
                if(type.value == val) {
                    return type;
                }
            }

            return NotificationType.UNKNOWN;
        }
    }
}
