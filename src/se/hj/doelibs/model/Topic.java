package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * @author Christoph
 */
public class Topic implements Serializable {

    private int topicId;
    private String name;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
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
