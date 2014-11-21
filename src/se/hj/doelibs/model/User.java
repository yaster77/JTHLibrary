package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * @author Christoph
 */
public class User implements Serializable {

    private int userId;
    private UserCategory category;
    private String email;
    private String firstName;
    private String lastName;
    private String room;
    private String uniPhone;

    public String getUniPhone() {
        return uniPhone;
    }

    public void setUniPhone(String uniPhone) {
        this.uniPhone = uniPhone;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
