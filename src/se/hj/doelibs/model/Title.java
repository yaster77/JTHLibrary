package se.hj.doelibs.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Christoph
 */
public class Title implements Serializable{

    private int titleId;
    private String bookTitle;
    private String isbn10;
    private String isbn13;
    private int editionNumber;
    private int editionYear;
    private int firstEditionYear;
    private Publisher publisher;

    private List<Author> authors;
    private List<Loanable> loanables;
    private List<Author> editors;
    private List<Topic> topics;

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public int getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(int editionYear) {
        this.editionYear = editionYear;
    }

    public int getFirstEditionYear() {
        return firstEditionYear;
    }

    public void setFirstEditionYear(int firstEditionYear) {
        this.firstEditionYear = firstEditionYear;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Loanable> getLoanables() {
        return loanables;
    }

    public void setLoanables(List<Loanable> loanables) {
        this.loanables = loanables;
    }

    public List<Author> getEditors() {
        return editors;
    }

    public void setEditors(List<Author> editors) {
        this.editors = editors;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
