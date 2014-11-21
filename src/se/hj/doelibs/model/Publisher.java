package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * @author Christoph
 */
public class Publisher implements Serializable {

    private int publisherId;
    private String name;

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
