package se.hj.doelibs.model;

import java.io.Serializable;

/**
 * Created by Christoph on 17.11.2014.
 */
public class UserCategory implements Serializable {

    private int categoryId;
    private String name;
    private int loanPeriod;

    /**
     * the ID of the admin category of DoeLibS
     */
    public static final int ADMIN_CATEGORY_ID = 1;

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

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }
}
