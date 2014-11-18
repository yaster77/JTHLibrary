package se.hj.doelibs.model;

import java.util.Date;

/**
 * @author Christoph
 */
public class Loan {

    private int loanId;
    private Loanable loanable;
    private User borrower;
    private Date borrowDate;
    private Date returnDate;
    private Date toBeReturnedDate;
    private Date recallExpiredDate;
    private RecallReason reasonForRecall;

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Loanable getLoanable() {
        return loanable;
    }

    public void setLoanable(Loanable loanable) {
        this.loanable = loanable;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getToBeReturnedDate() {
        return toBeReturnedDate;
    }

    public void setToBeReturnedDate(Date toBeReturnedDate) {
        this.toBeReturnedDate = toBeReturnedDate;
    }

    public Date getRecallExpiredDate() {
        return recallExpiredDate;
    }

    public void setRecallExpiredDate(Date recallExpiredDate) {
        this.recallExpiredDate = recallExpiredDate;
    }

    public RecallReason getReasonForRecall() {
        return reasonForRecall;
    }

    public void setReasonForRecall(RecallReason reasonForRecall) {
        this.reasonForRecall = reasonForRecall;
    }

    /**
     * enum for the reason of a recall
     */
    public enum RecallReason
    {
        RESERVATION(1),
        OWNER_RECALL(2),
        USER_EXPIRE(3),
        UNKNOWN(-1);

        private int value;
        private RecallReason(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }

        public static RecallReason getType(int val) {
            for (RecallReason type : RecallReason.values()) {
                if(type.value == val) {
                    return type;
                }
            }

            return RecallReason.UNKNOWN;
        }
    }
    
}
