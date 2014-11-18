package se.hj.doelibs.model;

/**
 * @author Christoph
 */
public class Loanable {    private int loanableId;
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
        AVAILABLE(0),

        /**
         * Book is currently borrowed
         */
        BORROWED(1),
        
        /**
         * the loanable is reserved
         */
        RESERVED(2),

        /**
         * the loanable is available, but locked from the owner cause of a recall/mark as unavailable 
         */
        UNAVAILABLE_FROM_OWNER(3),
 
        /**
         * the loanable is currently recalled (borrowed but ordered back from the owner)
         */
        RECALLED(4),
 
        /**
         * the loanable has been deleted
         */
        DELETED(5),
 
        /**
         * fallback
         */
        UNKNOWN(-1);

        private int value;
        private Status(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
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
