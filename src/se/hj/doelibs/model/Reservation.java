package se.hj.doelibs.model;

import java.util.Date;

/**
 * @author Christoph
 */
public class Reservation {

    private int reservationId;
    private Title title;
    private User user;
    private Date reserveDate;
    private Date availableDate;
    private boolean loanRecalled;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public boolean isLoanRecalled() {
        return loanRecalled;
    }

    public void setLoanRecalled(boolean loanRecalled) {
        this.loanRecalled = loanRecalled;
    }
}
