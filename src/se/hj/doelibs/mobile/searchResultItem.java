package se.hj.doelibs.mobile;


import java.io.Serializable;

public class SearchResultItem implements Serializable{
    public String title;
    public int titleId;

    @Override
    public String toString(){
        return String.format("%s", title);
    }
}
