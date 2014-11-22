package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * @author Christoph
 */
public class Loanable implements Serializable {
    private int loanableId;
    private String barcode;
    private LocationCategory category;
    private User owner;
    private String location;
    private Title title;
    private boolean unavailableFromOwner;
    private boolean deleted;
    private Status status;

    public int getLoanableId() {
        return loanableId;
    }

    public void setLoanableId(int loanableId) {
        this.loanableId = loanableId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public LocationCategory getCategory() {
        return category;
    }

    public void setCategory(LocationCategory category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public boolean isUnavailableFromOwner() {
        return unavailableFromOwner;
    }

    public void setUnavailableFromOwner(boolean unavailableFromOwner) {
        this.unavailableFromOwner = unavailableFromOwner;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * enum for status of a loanable
     */
    public enum Status {
        /**
         * Book is available
         */
        AVAILABLE(0, "Available"),

        /**
         * Book is currently borrowed
         */
        BORROWED(1, "Borrowed"),
        
        /**
         * the loanable is reserved
         */
        RESERVED(2, "Reserved"),

        /**
         * the loanable is available, but locked from the owner cause of a recall/mark as unavailable 
         */
        UNAVAILABLE_FROM_OWNER(3, "Unavailable"),
 
        /**
         * the loanable is currently recalled (borrowed but ordered back from the owner)
         */
        RECALLED(4, "Recalled"),
 
        /**
         * the loanable has been deleted
         */
        DELETED(5, "Deleted"),
 
        /**
         * fallback
         */
        UNKNOWN(-1, "Unknown");

        private int value;
        private String description;
        private Status(int val, String description) {
            this.value = val;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return description;
        }

        public static Status getType(int val) {
            for (Status type : Status.values()) {
                if(type.value == val) {
                    return type;
                }
            }

            return Status.UNKNOWN;
        }
    }
}
