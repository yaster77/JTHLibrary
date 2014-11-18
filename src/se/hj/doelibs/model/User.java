package se.hj.doelibs.model;

/**
 * @author Christoph
 */
public class User {

    private int UserId;
    private UserCategory Category;
    private String EMail;
    private String FirstName;
    private String LastName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public UserCategory getCategory() {
        return Category;
    }

    public void setCategory(UserCategory category) {
        Category = category;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
