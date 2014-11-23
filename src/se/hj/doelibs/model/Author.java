package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * Created by Christoph on 17.11.2014.
 */
public class Author implements Serializable {

    private int authorId;
    private String name;

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
