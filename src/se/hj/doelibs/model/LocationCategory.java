package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * @author Christoph
 */
public class LocationCategory implements Serializable {

    private int categoryId;
    private String name;
    private User owner;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
