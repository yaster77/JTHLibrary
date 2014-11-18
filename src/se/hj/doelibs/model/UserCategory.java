package se.hj.doelibs.model;

/**
 * Created by Christoph on 17.11.2014.
 */
public class UserCategory {

    private int categoryId;
    private String name;
    private int loanPeriod;

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
